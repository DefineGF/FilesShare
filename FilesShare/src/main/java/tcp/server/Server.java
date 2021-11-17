package tcp.server;

import bean.HostInfo;
import config.SystemConfig;
import listener.ServerCloseListener;
import tcp.client.Client;
import util.CloseUtil;
import util.Log;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server implements ServerCloseListener {
    private final static String TAG = "Server >> ";
    public final static Log Log  = new Log(TAG);

    private ServerSocket serverSocket;
    private final HashSet<HostTask> users; //保存所有的连接
    private boolean needExit = false;

    private HostInfo serverInfo;

    public Server() {
        users = new HashSet<>();
        ServerConsoleTask serverConsoleTask = new ServerConsoleTask(this);
        new Thread(serverConsoleTask).start();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(SystemConfig.SERVER_PORT);
            serverInfo = new HostInfo("SERVER", "127.0.0.1", SystemConfig.SERVER_PORT);
        } catch (IOException e) {
            Log.e("绑定端口失败!" + e.getMessage());
        }
        while(!needExit) {
            Socket client;
            try {
                client = serverSocket.accept();
                HostTask user = new HostTask(client);
                users.add(user);
                new Thread(user).start();
                Log.i_n(getClientsInfo());  // 输出 所有用户信息
            } catch (IOException e) {
                Log.e("服务器断开~" + e.getMessage());
                users.clear();
                break;
            }
        }
        System.out.println("program end");
    }



    /**
     * 获取在线用户信息 (包括 服务器 在内)
     * @return string
     */
    private String getClientsInfo() {
        StringBuilder sb = new StringBuilder("当前用户:\n");
        sb.append(serverInfo.toString()).append("\n");           // 含 Server 在内
        for (HostTask user : users) {
            sb.append(user.hostInfo.toString()).append("\n");
        }
        return sb.toString();
    }



    @Override
    public void close() {
        this.needExit = true;
        CloseUtil.closeAll(serverSocket);
    }

    /**
     * 负责信息接收 & 转发
     */
    private class HostTask implements Runnable {
        private final HostInfo hostInfo;
        private final Socket socket;

        private DataInputStream  clientDataIS;  // 来自 socket 数据流
        private DataOutputStream clientDataOS;  // 发送 socket 数据流

        private boolean isRunning = true;

        public HostTask(Socket socket) {
            this.socket = socket;
            hostInfo = new HostInfo("", socket.getInetAddress().getHostAddress(), socket.getPort());
            try {
                clientDataIS = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                clientDataOS = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                String name = clientDataIS.readUTF();
                hostInfo.setName(name);
                this.sendInfoToOthers("<" + name + " - " + socket.getInetAddress().getHostAddress() + " > joined!",true);
            } catch (IOException e) {
                e.printStackTrace();
                release();
            }
        }


        @Override
        public void run() {
            String msg = "";
            while(isRunning) {
                try {
                    msg = clientDataIS.readUTF();
                    int ans = handleMessage(msg);
                    if (ans == -1) {
                        release();
                        break;
                    }
                } catch (IOException e) {
                    Server.Log.e("中断连接：" + e.getMessage());
                    release(); // include break the while
                }
            }
        }

        private int handleMessage(String msg) throws IOException {
            if (msg == null || msg.equals("")) {
                return 0;
            } else if (SystemConfig.CMD_EXIT.equals(msg)) {        // exit 则 中断 while
                socket.shutdownInput();                            // 关闭输入流
                return -1;
            } else if (SystemConfig.CMD_SHOW_GROUP.equals(msg)) {  // "show"
                sendInfoToClient(getClientsInfo());
            } else if (msg.startsWith(SystemConfig.CMD_SEND_INFO)) { // send message to other socket
                handleSendInfo(msg);
            } else if (msg.startsWith(SystemConfig.CMD_SEND_FILE)) { // send file to other socket
                handleSendFile(msg);
            }
            return 1;
        }

        private void handleSendInfo(String msg) throws IOException { // format -> "sendi xxx @<target>" or "sendi xxx"
            if (msg.contains("@")) {
                int startI = msg.indexOf(" ");
                int endI = msg.lastIndexOf("@");
                String content = msg.substring(startI + 1, endI);
                String target  = msg.substring(endI + 1);
                HostTask user;
                if ((user = getUserTaskByName(target)) != null) {
                    user.sendInfoToClient(hostInfo.getName() + " : " + content);
                }
            } else {
                String content = msg.substring(msg.indexOf(" "));
                sendInfoToOthers(content, false);
                Log.i(hostInfo.getName() + " : " + content);
            }
        }

        /**
         * 向目标 客户端 转发文件内容
         * @param msg 命令行
         */
        private void handleSendFile(String msg) throws IOException {
            long fileSize = Long.parseLong(msg.substring(msg.lastIndexOf("-") + 1));
            String targetClientName = msg.substring(msg.lastIndexOf("@") + 1, msg.lastIndexOf("-") - 1);

            if (targetClientName.equals(serverInfo.getName())) {
                String fileName = msg.substring(msg.indexOf(" ") + 1, msg.indexOf("@") - 1);
                String fileSavaPath = SystemConfig.SAVE_PATH_DEFAULT + "\\" + fileName;
                DataOutputStream fileDOS = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileSavaPath)));
                int t;
                long psSize = 0;
                byte[] buf = new byte[SystemConfig.BUFFER_SIZE];
                while ((t = clientDataIS.read(buf)) != -1) {
                    psSize += t;
                    fileDOS.write(buf, 0, t);
                    fileDOS.flush();
                    Log.p(fileName + " 已经接收: " + (psSize * 100L / fileSize) + "%");
                    if (psSize >= fileSize) {
                        break;
                    }
                }
                fileDOS.close();             // 关闭文件输出流
                Log.i_n("文件保存成功");
            } else {
                HostTask target = getUserTaskByName(targetClientName);
                if (target != null) {
                    target.sendInfoToClient(msg); // 通知 目标客户端
                    int t;
                    int psSize = 0;
                    byte[] buf = new byte[SystemConfig.BUFFER_SIZE];
                    while((t = clientDataIS.read(buf)) != -1) {
                        target.sendFileBufToClient(buf, t);
                        psSize += t;
                        if (psSize == fileSize) {
                            break;
                        }
                    }
                    Log.i_n("转发完毕!");
                } else {
                    Log.e("目标不存在!");
                }
            }

        }

        /**
         * 提供 写入文件缓存接口
         * @param buf：文件 byte类型缓存
         * @param len: 缓存大小
         */
        public void sendFileBufToClient(byte[] buf, int len) throws IOException {
            clientDataOS.write(buf, 0, len);
            clientDataOS.flush();
        }

        public void sendInfoToClient(String msg) throws IOException { // 向 发送者 发送消息
            clientDataOS.writeUTF(msg);
            clientDataOS.flush();
        }

        public void sendInfoToOthers(String msg, boolean admin) throws IOException {
            for (HostTask client : users) {
                if(client != this) {
                    if(admin) {
                        client.sendInfoToClient("系统: " + msg);
                    } else {
                        client.sendInfoToClient(hostInfo.getName() + " : " + msg);
                    }
                }
            }
        }


        /**
         * break the while && clean the socket && recycle resource
         */
        private void release() {
            isRunning = false;
            users.remove(this);
            CloseUtil.closeAll(clientDataIS, clientDataOS, socket);
            Server.Log.e(hostInfo.getName() + " 离开系统 剩余用户: " + users.size());
            Log.i_n(getClientsInfo()); // 输出 所有用户信息
        }

        private HostTask getUserTaskByName(String name) {
            for (HostTask user : users) {
                if (user.hostInfo.getName().equals(name)) {
                    return user;
                }
            }
            return null;
        }

    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
