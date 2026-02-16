package com.mediview.domain.consultation.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediview.domain.consultation.entity.ConsultMessage;
import com.mediview.domain.consultation.entity.ConsultSession;
import com.mediview.domain.consultation.repository.ConsultMessageRepository;
import com.mediview.domain.consultation.repository.ConsultSessionRepository;
import com.mediview.domain.user.entity.User;
import com.mediview.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ConsultMessageRepository messageRepository;
    private final ConsultSessionRepository sessionRepository;
    private final UserRepository userRepository;

    private final Map<String, Set<WebSocketSession>> sessionRooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = extractSessionId(session);
        sessionRooms.computeIfAbsent(sessionId, k -> new CopyOnWriteArraySet<>()).add(session);
        log.info("Chat WebSocket connected: sessionId={}", sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = extractSessionId(session);
        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);

        ConsultSession consultSession = sessionRepository.findById(Long.parseLong(sessionId)).orElse(null);
        if (consultSession == null) {
            session.sendMessage(new TextMessage("{\"error\":\"Session not found\"}"));
            return;
        }

        if (chatMessage.getSenderId() != null) {
            User sender = userRepository.findById(chatMessage.getSenderId()).orElse(null);
            if (sender != null) {
                ConsultMessage msg = ConsultMessage.builder()
                        .session(consultSession)
                        .sender(sender)
                        .msgType(chatMessage.getMsgType() != null ? chatMessage.getMsgType() : "TEXT")
                        .content(chatMessage.getContent())
                        .build();
                messageRepository.save(msg);
            }
        }

        chatMessage.setTimestamp(LocalDateTime.now().toString());
        String payload = objectMapper.writeValueAsString(chatMessage);

        Set<WebSocketSession> room = sessionRooms.get(sessionId);
        if (room != null) {
            for (WebSocketSession ws : room) {
                if (ws.isOpen()) {
                    ws.sendMessage(new TextMessage(payload));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String sessionId = extractSessionId(session);
        Set<WebSocketSession> room = sessionRooms.get(sessionId);
        if (room != null) {
            room.remove(session);
            if (room.isEmpty()) {
                sessionRooms.remove(sessionId);
            }
        }
        log.info("Chat WebSocket disconnected: sessionId={}", sessionId);
    }

    private String extractSessionId(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) return "unknown";
        String path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
