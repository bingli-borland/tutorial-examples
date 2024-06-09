package com.techprimers.springbootwebsocketexample.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component("imWebsocketSessionManager")
public class IMWebsocketSessionManager {

    private static final String beanName = "imWebsocketSessionManager";

    public static String getBeanName() {
        return beanName;
    }

    private static ConcurrentHashMap<String, String> sessionIdCust = new ConcurrentHashMap<String, String>();

    private static ConcurrentHashMap<String, WebSocketSession> agentSessionManager = new ConcurrentHashMap<String, WebSocketSession>();
    private static ConcurrentHashMap<String, WebSocketSession> customerSessionManager = new ConcurrentHashMap<String, WebSocketSession>();

    public void addSessionIdCust(String sessionId, String custNo, String agentId) {
        sessionIdCust.put(sessionId, custNo);
    }

    public String getSessionIdCust(String sessionId) {
        String string = sessionIdCust.get(sessionId);
        if (string != null) {
            return sessionIdCust.get(sessionId);
        }
        return null;
    }

    public void addCustomerSession(String custNo, WebSocketSession session) {
        customerSessionManager.put(custNo, session);
    }

    public void removeCustomerSession(String custNo) {
        customerSessionManager.remove(custNo == null ? "" : custNo);
    }

    public WebSocketSession getCustomerSession(String custNo) {
        return customerSessionManager.get(custNo);
    }

    /**
     * 发送坐席消息给客户 1.session在当前服务器，直接发送 2.session不在当前服务器，转到到另一台服务器
     * @param custNo
     * @param message
     * @return
     */
    public String sendMessage2Customer(String custNo, String message) {
        try {
            WebSocketSession session = getCustomerSession(custNo);
            if (session != null) {
                if (session.isOpen()) {
                    synchronized (session) {
                        session.sendMessage(new TextMessage(message));
                    }
                } else {
                    System.out.println("目标客户session在当前服务器，session状态为关闭！");
                }
                return "1";
            }
            //
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }

}
