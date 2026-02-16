package com.mediview.domain.auditlog.service;

import com.mediview.domain.auditlog.dto.AuditLogResponse;
import com.mediview.domain.auditlog.entity.AuditLog;
import com.mediview.domain.auditlog.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public void log(Long actorId, String action, String targetType, Long targetId,
                    String ip, String userAgent, String metadataJson) {
        AuditLog auditLog = AuditLog.builder()
                .actorId(actorId)
                .action(action)
                .targetType(targetType)
                .targetId(targetId)
                .ip(ip)
                .userAgent(userAgent)
                .metadataJson(metadataJson)
                .build();
        auditLogRepository.save(auditLog);
    }

    @Override
    public List<AuditLogResponse> getAllLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private AuditLogResponse toResponse(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .actorId(log.getActorId())
                .action(log.getAction())
                .targetType(log.getTargetType())
                .targetId(log.getTargetId())
                .ip(log.getIp())
                .metadataJson(log.getMetadataJson())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
