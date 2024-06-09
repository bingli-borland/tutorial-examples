package com.techprimers.springbootwebsocketexample.config;

import com.techprimers.springbootwebsocketexample.entity.IMChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

public class IMCustNlpWelcomeService {

    @Autowired
    private ApplicationContext applicationContext;

    public void handleMessage(IMChatMessage imChatMessage) {
        IMWebsocketSessionManager websocketSessionManager = (IMWebsocketSessionManager)
                ApplicationContextProvider.getApplicationContext().getBean(IMWebsocketSessionManager.getBeanName());
        websocketSessionManager.sendMessage2Customer(imChatMessage.getCustNo(), "hello");
    }

    public void setSpringBeanContextUtil() {

    }

}
