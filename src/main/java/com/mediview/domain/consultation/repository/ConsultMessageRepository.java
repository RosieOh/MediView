package com.mediview.domain.consultation.repository;

import com.mediview.domain.consultation.entity.ConsultMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultMessageRepository extends JpaRepository<ConsultMessage, Long> {

    List<ConsultMessage> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
}
