package com.mediview.domain.intake.repository;

import com.mediview.domain.intake.entity.IntakeForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntakeFormRepository extends JpaRepository<IntakeForm, Long> {

    List<IntakeForm> findByAppointmentId(Long appointmentId);
}
