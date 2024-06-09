package com.techprimers.springbootwebsocketexample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class IMWebsocket implements WebSocketConfigurer {

    @Autowired
    IMCustHandler custHandler;

    @Autowired
    IMHandshakeInterceptor interceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry register) {
        System.out.println("初始化websocket配置，配置接入点：/cust");
        register.addHandler(custHandler, "/cust").setAllowedOrigins("*").addInterceptors(interceptor);
    }

}
