package com.mediview.domain.notification.dto;

import com.mediview.domain.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {

    private Long id;
    private NotificationType type;
    private String title;
    private String content;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
