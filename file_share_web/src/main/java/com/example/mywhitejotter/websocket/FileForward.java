package com.example.mywhitejotter.websocket;

import com.example.mywhitejotter.bean.RequestState;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint(value="/file_forward/{name}")
@Component
public class FileForward {
    private static final String ASK = "ASK:"; // 请求文件: ASK:'file_name';'target'
    private static final String RES = "RES:"; // 同意文件请求, 并附上文件内容：RES:'file_name';'file_size';'target'
    private static final String REF = "REF:"; // 拒绝请求 REF:'target_id';(reason)


    public static List<FileForward> fileForwardList = new CopyOnWriteArrayList<>();

    private String       userName;
    private Session      session;
    private RequestState requestState;

    @OnOpen
    public void onOpen(Session session, @PathParam("name")String userName) {
        if (userName == null || "".equals(userName)) { // 避免重复连接
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("user_id is null: " + e.getMessage());
            }
            return;
        }
        session.setMaxBinaryMessageBufferSize(50 * 1024 * 1024);
        this.session = session;
        this.userName = userName;
        requestState = new RequestState();
        fileForwardList.add(this);
        System.out.println(userName + " 连接成功！");
    }

    @OnMessage
    public void onMessage(byte[] messages, Session session) {
        if (!requestState.isReqFile()) {
            String message = new String(messages, StandardCharsets.UTF_8);
            System.out.println("server: get message is = " + message);
            if(message.startsWith(ASK)) {
                handleASK(message);
            } else if (message.startsWith(RES)) {
                handleRES(message);
            } else if (message.startsWith(REF)) {
                handREF(message);
            }
        } else {
            System.out.println("server: 正发送文件~");
            FileForward target = getTargetById(requestState.getTargetId());
            if (target != null) {
                try {
                    target.getSession().getBasicRemote().sendBinary(ByteBuffer.wrap(messages));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("文件发送完毕!");
        }
    }

    /**
     * 一客户端请求文件连接
     * @param msg 请求完整信息
     */
    private void handleASK(String msg) {
        String fileName = msg.substring(msg.indexOf(":") + 1, msg.lastIndexOf(";"));
        String targetID = msg.substring(msg.lastIndexOf(";") + 1);
        FileForward target = getTargetById(targetID);
        if (target != null) {
            String temp = ASK + fileName + ";" + userName; // 向目标客户端发送 ASK:'file_name';'source' 表示某客户端请求文件
            sendMsg(target.getSession(), temp);
            System.out.println("服务器向: " + targetID + " 发送：" + temp);
        }
    }

    /**
     * 被请求者 同意发送文件
     * @param msg 回复完整信息 RES:file_name;file_size;target
     */
    private void handleRES(String msg) {
        String temp = msg.substring(msg.indexOf(":") + 1);
        String[] content = temp.split(";");
        requestState.setReqFile(true);                             // 标定正在转发文件
        requestState.setReqFileName(content[0]);                   // 获取转发文件名
        requestState.setReqFileSize(Long.parseLong(content[1]));   // 获取文件大小
        requestState.setTargetId(content[2]);                      // 获取目标主机ID
        FileForward target = getTargetById(content[2]);
        if (target != null) {
            String t = RES + requestState.getReqFileName() + ";" + requestState.getReqFileSize();
            sendMsg(target.getSession(), t);
        }
    }

    /**
     * 处理拒绝
     * @param msg REF:'target_id';('reason')
     */
    private void handREF(String msg) {
        String targetId = msg.substring(msg.indexOf(":") + 1, msg.indexOf(";"));
        FileForward target = getTargetById(targetId);
        sendMsg(target.getSession(), REF + "你被拒绝了!");
    }


    public void sendMsg(Session session, String msg) {
        try {
            session.getBasicRemote().sendBinary(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileForward getTargetById(String targetId) {
        for (FileForward f : fileForwardList) {
            if (f.getUserName().equals(targetId)) {
                return f;
            }
        }
        return null;
    }

    @OnClose
    public void onClose() {
        fileForwardList.remove(this);
        System.out.println("关闭连接!");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误!");
        error.printStackTrace();
    }

    public Session getSession() {
        return this.session;
    }

    public String getUserName() {
        return userName;
    }
}
