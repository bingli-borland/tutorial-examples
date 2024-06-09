package com.techprimers.springbootwebsocketexample.service;

import com.techprimers.springbootwebsocketexample.config.IMCustNlpWelcomeService;
import com.techprimers.springbootwebsocketexample.entity.IMChatMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

@Service
public class IMMessageDispatcher {

    public void dispatcher(TextMessage message) {
        IMChatMessage imChatMessage = new IMChatMessage();
        String custNo = message.getPayload();
        imChatMessage.setCustNo(custNo);
        imChatMessage.setPlayload(message.getPayload());
        IMCustNlpWelcomeService imCustNlpWelcomeService = new IMCustNlpWelcomeService();
        imCustNlpWelcomeService.handleMessage(imChatMessage);
    }
}
