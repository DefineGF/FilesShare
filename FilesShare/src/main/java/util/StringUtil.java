package util;

import config.SystemConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 命令行格式化
     * @param in 命令行
     * @return 格式化之后的命令行：首尾无空格 & 中间无连续空格
     */
    public static String stringStandard(String in) {
        return in.trim().replaceAll("\\s{1,}", " ");
    }

    /**
     * 命令行提取 文件路径
     * @param message such as "sendf file-path @target"
     * @return file_path
     */
    public static String getPathFromCmd(String message) { // such as sendf F://text.txt @li
        message = stringStandard(message);
        int startI = message.indexOf(" ");
        int endI   = message.lastIndexOf("@");
        return message.substring(startI + 1, endI - 1);
    }

    public static String getNameFromPath(String path) { // 有 '\\' 和 '/' 两种形式
        int i1 = path.lastIndexOf("/");
        int i2 = path.lastIndexOf("\\");
        i1 = Math.max(i1, i2);
        return path.substring(i1 + 1);
    }

    /**
     * @param msg client command
     * @param size transfer file_size
     * @return the format such as "sendf effective java.pdf @li -1024"
     */
    public static String generateSendFMsg(String msg, long size) {
        String path     = getPathFromCmd(msg);
        String fileName = getNameFromPath(path);
        String target   = msg.substring(msg.lastIndexOf("@") + 1);
        return SystemConfig.getSendFileMsg(fileName, target, size);
        // return "sendf " + fileName + " " + msg.substring(msg.lastIndexOf("@")) + " -" + size;
    }

    public static void logIpMsg(Map<String, List<String>> map) {
        map.forEach((key, value) -> {
            System.out.print("网络接口名:" + key);
            System.out.println("\t" + Arrays.toString(value.toArray()));
        });
    }

    /**
     * 判断字符串是否是 IP 地址
     * @param addr 输入的IP地址
     * @return boolean
     */
    public boolean isIP(String addr) {
        if ("".equals(addr) || addr.length() < 7 || addr.length() > 15) {
            return false;
        }
        String rexp =
                "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        return mat.find();
    }
}
