package util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class InetUtil {
    public static String getHostAddress() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }


    /**
     * for windows
     */
    public static void logIps() {
        Map<String, List<String>> netMsgMap = new HashMap<>();  // 网路接口信息
        try {
            Enumeration<NetworkInterface> faces = NetworkInterface.getNetworkInterfaces();
            while (faces.hasMoreElements()) {                   // 遍历网络接口
                NetworkInterface face = faces.nextElement();
                if (face.isLoopback() || face.isVirtual() || !face.isUp()) {
                    continue;
                }
                List<String> ips  = new LinkedList<>();
                Enumeration<InetAddress> address = face.getInetAddresses();
                while (address.hasMoreElements()) {             // 遍历网络地址
                    InetAddress addr = address.nextElement();
                    if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress() && !addr.isAnyLocalAddress()) {
                        ips.add(addr.getHostAddress());
                    }
                }
                netMsgMap.put(face.getDisplayName(), ips);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        StringUtil.logIpMsg(netMsgMap);
    }
}
