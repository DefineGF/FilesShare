package tcp.server;

import config.SystemConfig;
import listener.ServerCloseListener;

import java.util.Scanner;

public class ServerConsoleTask implements Runnable {
    private final ServerCloseListener listener;

    public ServerConsoleTask(ServerCloseListener listener) {
        this.listener = listener;
    }
    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String in = sc.nextLine();
            if (SystemConfig.CMD_EXIT.equals(in)) {
                this.listener.close();
                sc.close();
                break;
            }
        }
    }
}
