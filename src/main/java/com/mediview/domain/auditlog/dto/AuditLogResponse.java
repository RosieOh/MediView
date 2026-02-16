package com.mediview.domain.auditlog.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditLogResponse {

    private Long id;
    private Long actorId;
    private String action;
    private String targetType;
    private Long targetId;
    private String ip;
    private String metadataJson;
    private LocalDateTime createdAt;
}
