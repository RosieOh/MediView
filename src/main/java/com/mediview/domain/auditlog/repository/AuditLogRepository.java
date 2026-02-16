package com.mediview.domain.auditlog.repository;

import com.mediview.domain.auditlog.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByActorId(Long actorId);

    List<AuditLog> findByTargetTypeAndTargetId(String targetType, Long targetId);
}
