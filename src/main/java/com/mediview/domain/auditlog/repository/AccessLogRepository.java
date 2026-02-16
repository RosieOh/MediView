package com.mediview.domain.auditlog.repository;

import com.mediview.domain.auditlog.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    List<AccessLog> findByActorId(Long actorId);
}
