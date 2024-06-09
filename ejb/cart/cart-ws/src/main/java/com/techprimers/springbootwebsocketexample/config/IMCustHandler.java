package com.techprimers.springbootwebsocketexample.config;

import com.techprimers.springbootwebsocketexample.service.IMMessageDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;

@Component
public class IMCustHandler extends TextWebSocketHandler {

    @Autowired
    IMMessageDispatcher imMessageDispatcher;

    @Autowired
    IMWebsocketSessionManager imWebsocketSessionManager;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String custNo = (String) session.getAttributes().get("custNo");
        if (StringUtils.isEmpty(custNo)) {
            session.close();
            return;
        }
        imWebsocketSessionManager.addCustomerSession(custNo, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获得客户端传来的消息
        String payload = message.getPayload();
        Object custNo = session.getAttributes().get("custNo");
        System.out.println("server 接收到 " + custNo + " 发送的 " + payload);


        if (message.getPayload().equals("ping")) {
            WebSocketSession session1= session;
            synchronized (session1) {
                session.sendMessage(new TextMessage("pong"));
            }
        } else {
            imMessageDispatcher.dispatcher(message);
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String custNo = imWebsocketSessionManager.getSessionIdCust(session.getId());
        imWebsocketSessionManager.removeCustomerSession(custNo);
    }
}
