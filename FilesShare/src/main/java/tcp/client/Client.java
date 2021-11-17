package tcp.client;

import config.SystemConfig;
import util.CloseUtil;
import util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private static final String TAG = "CLIENT >> ";
    private final BufferedReader console;
    private Socket client;

    public static final Log Log = new Log(TAG);

    public Client() {
        console = new BufferedReader(new InputStreamReader(System.in)); //从控制台获得输入
        Log.i("login as: ");
    }

    public void start() {
        String name;
        try {
            name = console.readLine();
            if(name.equals("") || name.equals("exit")) {
                Log.e("初始化失败！");
                CloseUtil.closeAll(console);
                return;
            }
            client = new Socket("localhost", SystemConfig.SERVER_PORT);  //建立新连接，注意这里创建好就已经连接上了，要保证服务端已经开启
            Log.i_n("连接成功!");
            new Thread(new ClientSendTask(client, name)).start();
            new Thread(new ClientReceiveTask(client)).start();
        } catch (IOException e) {
            CloseUtil.closeAll(console, client);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
