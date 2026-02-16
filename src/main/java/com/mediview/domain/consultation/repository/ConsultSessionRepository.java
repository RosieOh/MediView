package com.mediview.domain.consultation.repository;

import com.mediview.domain.consultation.entity.ConsultSession;
import com.mediview.domain.enums.ConsultStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultSessionRepository extends JpaRepository<ConsultSession, Long> {

    Optional<ConsultSession> findByAppointmentId(Long appointmentId);

    List<ConsultSession> findByStatus(ConsultStatus status);
}
