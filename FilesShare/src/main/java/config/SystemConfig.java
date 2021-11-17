package config;

import util.Log;


public class SystemConfig {
    public static final int SERVER_PORT       = 8888;
    public static final int BUFFER_SIZE       = 1024 * 2;

    public static String SAVE_PATH_DEFAULT = "G:\\1_receive";
    public static String TARGET_IP = "127.0.0.1";

    public static final String SAVE_PATH      = "save "; // 设置保存路径
    public static final String CMD_IPCONFIG   = "ipconfig";
    public static final String CMD_LINK       = "link ";

    // client 请求头
    public static final String CMD_HELP       = "help";
    public static final String CMD_SHOW_GROUP = "show";
    public static final String CMD_SEND_INFO  = "sendi ";
    public static final String CMD_SEND_FILE  = "sendf ";
    public static final String CMD_EXIT       = "exit";

    // server 响应头
    public static final String RESPONSE_HEAD_USERS = "Users:";
    public static final String RESPONSE_HEAD_JOIN  = "Join:";
    public static final String RESPONSE_HEAD_EXIT  = "Exit:";
    public static final String RESPONSE_HEAD_MSG   = "Msg:";
    public static final String RESPONSE_HEAD_FILE  = "File:";
    public static final String RESPONSE_HEAD_NAME  = "Name:";

    public static String getSendFileMsg(String fileName, String target, long size) {
        return CMD_SEND_FILE + fileName + " @" + target + " -" + size;
    }

    public static void logCmd() {
        Log LOG = new Log("\t");
        LOG.i_n(SystemConfig.CMD_IPCONFIG + " -> 'ipconfig' to show local host ip(s)");
        LOG.i_n(SystemConfig.CMD_EXIT + " -> 'exit' to close tcp connection");
        LOG.i_n(SystemConfig.CMD_SHOW_GROUP + " -> 'show' to show connectors");
        LOG.i_n(SystemConfig.CMD_LINK + " -> 'link <target_ip>' to connect one friend");
        LOG.i_n(SystemConfig.CMD_SEND_INFO + " -> 'sendi xxx' @<name> or 'sendi xxx' to send messages");
        LOG.i_n(CMD_SEND_FILE + " -> 'sendf <file_path> @<name>' to send file");
    }

}
