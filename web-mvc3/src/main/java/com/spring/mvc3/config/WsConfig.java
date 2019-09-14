package com.spring.mvc3.config;

import com.spring.mvc3.webSocket.ChatWebSocketHandler;
import com.spring.mvc3.webSocket.EchoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Configuration
@EnableWebSocket
public class WsConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(textWebSocketHandler(),"/echo");
        webSocketHandlerRegistry.addHandler(chatWebSocketHandler(),"/chat");
    }

    @Bean
    public WebSocketHandler chatWebSocketHandler(){
        return new ChatWebSocketHandler();
    }

    @Bean
    public WebSocketHandler textWebSocketHandler() {
        return new EchoHandler();
    }
}
