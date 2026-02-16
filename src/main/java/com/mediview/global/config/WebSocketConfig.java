package com.mediview.global.config;

import com.mediview.domain.consultation.websocket.ChatWebSocketHandler;
import com.mediview.domain.consultation.websocket.WebRtcSignalingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final WebRtcSignalingHandler webRtcSignalingHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/ws/chat/{sessionId}")
                .setAllowedOrigins("*");

        registry.addHandler(webRtcSignalingHandler, "/ws/webrtc/{roomId}")
                .setAllowedOrigins("*");
    }
}
