package bean;

public class HostInfo {
    private String name;        // 记录连接用户的名字
    private String ip;          // 客户端 ip 地址
    private int  port;          // 客户端 端口

    public HostInfo() {

    }

    public HostInfo(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "name:" + name + ",ip:" + ip + ",port:" + port;
    }

    public static HostInfo createHostInfoByStr(String content) {
        String[] infos = content.split(",");
        String name = infos[0].substring(infos[0].indexOf(":") + 1);
        String ip   = infos[1].substring(infos[1].indexOf(":") + 1);
        int    port = Integer.parseInt(infos[2].substring(infos[2].indexOf(":") + 1));

        return new HostInfo(name, ip, port);
    }
}
