package com.spring.mvc3.webSocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private Map<String, WebSocketSession> users = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log(session.getId()+" 연결 됨");
        users.put(session.getId(),session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log(session.getId()+" 연결 종료");
        users.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log(session.getId()+"로부터 메시지 수신: "+message.getPayload());
        for(WebSocketSession user : users.values()){
            user.sendMessage(message);
            log(user.getId()+"에 메시지 발송:"+message.getPayload());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log(session.getId()+" 익셉션 발생: "+exception.getMessage());
    }

    private void log(String msg){
        System.out.println(new Date()+" : "+msg);
    }
}
