package com.mediview.domain.auditlog.service;

import com.mediview.domain.auditlog.dto.AuditLogResponse;

import java.util.List;

public interface AuditLogService {

    void log(Long actorId, String action, String targetType, Long targetId, String ip, String userAgent, String metadataJson);

    List<AuditLogResponse> getAllLogs();
}
