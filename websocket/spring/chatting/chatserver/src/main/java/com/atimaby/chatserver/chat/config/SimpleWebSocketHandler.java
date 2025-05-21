package com.atimaby.chatserver.chat.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.rmi.server.LogStream.log;

// connect로 웹소켓 연결요청이 들어왔을 때, 이를 처리할 클래스
@Component
@Log4j2
public class SimpleWebSocketHandler extends TextWebSocketHandler {

    // 연결된 세션 관리 : 스레드 safe한 set사용
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log("Connected : " + session.getId());
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log("Received Message : " + payload);

        for (WebSocketSession s : sessions) {
            if(s.isOpen()) {
                s.sendMessage(new TextMessage(payload));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log("Disconnected !");
    }

}
