package tcp.client;

import config.SystemConfig;
import util.CloseUtil;
import util.InetUtil;
import util.StringUtil;

import java.io.*;
import java.net.Socket;

/**
 * 创建 BufferedReader 输入流：监控控制台输入；
 * 持有 Socket 输出流：通过 send 方法向 server发送消息；
 */
public class ClientSendTask implements Runnable {
    private DataOutputStream dataOS;       // socket输出
    private BufferedReader   console;      // 控制台读入
    private final Socket     client;


    public ClientSendTask(final Socket client, String name) {
        this.client = client;
        try {
            dataOS  = new DataOutputStream(client.getOutputStream());
            console = new BufferedReader(new InputStreamReader(System.in));
            sendInfoToServer(name);
        } catch (IOException e) {
            CloseUtil.closeAll(console, dataOS, client);
            Client.Log.e("连接失败~");
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                String command = console.readLine();          // 阻塞 read
                int ans = handleCommand(command);             // 处理命令
                if (ans == 0) {                               // such as null
                    Client.Log.i_n("input again~");
                    continue;
                }
                if (ans == -1)  // such as "exit"
                    break;
            } catch (IOException e) {
                CloseUtil.closeAll(console, dataOS, client);
                Client.Log.e("send task has exception: " + e.getMessage());
                break;
            }
        }

    }

    private int handleCommand(String cmd) throws IOException {
        if (cmd == null || cmd.equals("")) {
            return 0;
        }
        String command = cmd.trim();
        if (command.contains(" ")) {
            if (command.startsWith(SystemConfig.CMD_SEND_INFO)) {        // 发送消息 "sendi xxx @<target>" or "sendi xxx"
                sendInfoToServer(command);
            } else if (command.startsWith(SystemConfig.CMD_SEND_FILE)) { // 发送文件
                return sendFile(command);
            } else {
                Client.Log.e("input command <help> for more message!");
            }
        } else {  // 单命令
            switch (command) {
                case SystemConfig.CMD_SHOW_GROUP:
                    sendInfoToServer("show");
                    break;
                case SystemConfig.CMD_EXIT:
                    sendInfoToServer(SystemConfig.CMD_EXIT);
                    client.shutdownInput();
                    client.shutdownOutput();
                    return -1;
                case SystemConfig.CMD_IPCONFIG:
                    InetUtil.logIps();
                    break;
                case SystemConfig.CMD_HELP:
                    SystemConfig.logCmd();
                    break;
                default:
                    Client.Log.e("input command <help> for more message!");
                    break;
            }
        }
        return 1;
    }

    //发送函数
    public void sendInfoToServer(String msg) throws IOException {
        dataOS.writeUTF(msg);
        dataOS.flush();
    }

    /**
     *
     * @param command console input command line
     * @return 0: command is null | file not found; 1: send file successful
     * @throws IOException NullException
     */
    private int sendFile(String command) throws IOException {
        if (command == null || !command.contains("@")) {
            return 0;
        }
        command = StringUtil.stringStandard(command);     // 消息格式化
        String path = StringUtil.getPathFromCmd(command); // 获取文件路径

        File file = new File(path);
        if (!file.exists()) {
            Client.Log.e("file not exists");
            return 0;
        }
        String sendToServerMsg = StringUtil.generateSendFMsg(command, file.length()); // "sendf <file_name> @<target> -<size>"
        sendInfoToServer(sendToServerMsg);
        Client.Log.i_n("向服务器发送：" + path);

        int t;
        int psSize = 0;
        long totalSize = file.length();
        byte[] sdBuf = new byte[SystemConfig.BUFFER_SIZE];
        DataInputStream fileDataIS = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        while ((t = fileDataIS.read(sdBuf)) != -1) {
            psSize += t;
            dataOS.write(sdBuf, 0, t);
            Client.Log.p(file.getName() + " 已经发送: " + psSize * 100L / totalSize + "%");
        }
        dataOS.flush();
        fileDataIS.close();  // 关闭文件输入流
//        dataOS.write(Config.FILE_END_BYTES, 0, Config.FILE_END_BYTES.length);
//        dataOS.flush();

        System.out.println();
        Client.Log.e(file.getName() + " 发送成功");
        return 1;
    }
}
