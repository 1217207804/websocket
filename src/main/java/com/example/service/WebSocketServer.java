package com.example.service;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gxq
 * @Description
 * @create 2020-08-19 9:51
 */
@Component
@ServerEndpoint("/webSocket/{sid}")
public class WebSocketServer {
    // 静态变量，用来记录当前在线链接数，应该把他设计成线程安全的
    private static AtomicInteger onlineNum = new AtomicInteger();
    // concurrent包的线程安全set，用来存放每个客户端对应的webSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<String, Session>();

    // 发送消息
    public void sendMessage(Session session, String message) throws IOException {
        if(session != null) {
            synchronized (session) {
                System.out.println("发送数据："+ message);
                session.getBasicRemote().sendText(message);
            }
        }
    }

    // 给指定用户发送信息
    public void sendInfo(String userName, String message) throws IOException {
        Session session = sessionPools.get(userName);
        sendMessage(session, message);
    }

    // 建立链接成功调用
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "sid") String userName) {
        System.out.println("websocket期望建立链接");
        sessionPools.put(userName, session);
        addOnlineCount();
        System.out.println(userName + "加入webSocket！当前人数为" + onlineNum);
        try {
            sendMessage(session, "欢迎" + userName + "加入链接");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //关闭链接时调用
    @OnClose
    public void onClose(@PathParam(value = "sid") String userName){
        sessionPools.remove(userName);
        subOnlineCount();
        System.out.println(userName + "断开webSocket链接！当前人数为" + onlineNum);
    }

    //收到客户端信息
    @OnMessage
    public void onMessage(String message) {
        message = "客户端：" + message + ",已收到";
        System.out.println(message);
        for (Session session: sessionPools.values()){
            try {
                sendMessage(session, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

















    public static void addOnlineCount() {
        onlineNum.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }


}
