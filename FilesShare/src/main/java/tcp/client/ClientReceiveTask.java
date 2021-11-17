package tcp.client;

import config.SystemConfig;
import util.CloseUtil;

import java.io.*;
import java.net.Socket;

/**
 * 持有 socket 输入流: 阻塞接收 server 的消息；
 */
public class ClientReceiveTask implements  Runnable {
    private final Socket clientSocket;

    private volatile boolean isRun = true;   //线程标识
    private DataInputStream  dis;            //负责读取服务端发送过来的信息



    public ClientReceiveTask(Socket client) {
        this.clientSocket = client;
        try {
            dis = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            CloseUtil.closeAll(dis, client);
            isRun = false;
        }
    }
    @Override
    public void run() {
        String msg;
        while(isRun){
            try {
                msg = dis.readUTF();
                handleServerMsg(msg);
            } catch (IOException e) {
                Client.Log.e("连接中断~");
                CloseUtil.closeAll(dis, clientSocket);
                break;
            }
        }
        CloseUtil.closeAll(dis, clientSocket);
    }

    private void handleServerMsg(String msg) throws IOException {
        if (msg == null || "".equals(msg)) {
            return;
        }
        if (msg.startsWith(SystemConfig.CMD_SEND_FILE)) {
            receiveFile(msg);
        } else {
            Client.Log.i_n(msg);
        }
    }

    /**
     *
     * @param msg from server, such as "sendf <file_path> @<target> -'file_size'"
     * @throws IOException:
     */
    private void receiveFile(String msg) throws IOException {
        String fileName = msg.substring(msg.indexOf(" ") + 1, msg.indexOf("@") - 1);
        String fileSavePath = SystemConfig.SAVE_PATH_DEFAULT + "\\" + fileName;

        long fileSize = Long.parseLong(msg.substring(msg.lastIndexOf("-") + 1));
        DataOutputStream fileDOS = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileSavePath)));
        int t;
        long psSize = 0;
        byte[] buf = new byte[SystemConfig.BUFFER_SIZE];
        while ((t = dis.read(buf)) != -1) {
            psSize += t;
            fileDOS.write(buf, 0, t);
            fileDOS.flush();
            Client.Log.p(fileName + " 已经接收: " + (psSize * 100L / fileSize) + "%");
            if (psSize >= fileSize) {
                break;
            }
        }
        fileDOS.close();      // 关闭文件输出流
        Client.Log.i_n("文件接收完毕!");
    }
}
