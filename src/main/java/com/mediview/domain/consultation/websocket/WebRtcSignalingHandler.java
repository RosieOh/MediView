package com.mediview.domain.consultation.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebRTC signaling server over WebSocket.
 * Handles: offer, answer, ice-candidate, join, leave
 *
 * Protocol:
 * Client sends JSON: {"type": "offer|answer|ice-candidate|join|leave", "payload": {...}}
 * Server broadcasts to all other peers in the same room.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WebRtcSignalingHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = extractRoomId(session);
        Set<WebSocketSession> room = rooms.computeIfAbsent(roomId, k -> new CopyOnWriteArraySet<>());
        room.add(session);
        log.info("WebRTC peer joined room={}, peers={}", roomId, room.size());

        // Notify existing peers that a new peer joined
        String joinMsg = objectMapper.writeValueAsString(Map.of(
                "type", "peer-joined",
                "peerId", session.getId(),
                "peerCount", room.size()
        ));
        broadcastToOthers(roomId, session, joinMsg);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = extractRoomId(session);
        JsonNode json = objectMapper.readTree(message.getPayload());
        String type = json.has("type") ? json.get("type").asText() : "unknown";

        log.debug("WebRTC signal: room={}, type={}, from={}", roomId, type, session.getId());

        // Add sender info and forward to all other peers in the room
        String enriched = objectMapper.writeValueAsString(Map.of(
                "type", type,
                "senderId", session.getId(),
                "payload", json.has("payload") ? json.get("payload") : objectMapper.createObjectNode()
        ));

        broadcastToOthers(roomId, session, enriched);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = extractRoomId(session);
        Set<WebSocketSession> room = rooms.get(roomId);
        if (room != null) {
            room.remove(session);
            log.info("WebRTC peer left room={}, remaining={}", roomId, room.size());

            String leaveMsg = objectMapper.writeValueAsString(Map.of(
                    "type", "peer-left",
                    "peerId", session.getId(),
                    "peerCount", room.size()
            ));
            broadcastToAll(roomId, leaveMsg);

            if (room.isEmpty()) {
                rooms.remove(roomId);
            }
        }
    }

    private void broadcastToOthers(String roomId, WebSocketSession sender, String message) throws Exception {
        Set<WebSocketSession> room = rooms.get(roomId);
        if (room == null) return;
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession peer : room) {
            if (peer.isOpen() && !peer.getId().equals(sender.getId())) {
                peer.sendMessage(textMessage);
            }
        }
    }

    private void broadcastToAll(String roomId, String message) throws Exception {
        Set<WebSocketSession> room = rooms.get(roomId);
        if (room == null) return;
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession peer : room) {
            if (peer.isOpen()) {
                peer.sendMessage(textMessage);
            }
        }
    }

    private String extractRoomId(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) return "unknown";
        String path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
