package com.mediview.domain.consultation.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private String type;
    private Long sessionId;
    private Long senderId;
    private String senderName;
    private String msgType;
    private String content;
    private String timestamp;
}
