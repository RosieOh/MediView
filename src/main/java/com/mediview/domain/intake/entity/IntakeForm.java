package com.mediview.domain.intake.entity;

import com.mediview.domain.appointment.entity.Appointment;
import com.mediview.domain.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_intake_form")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntakeForm extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(columnDefinition = "TEXT")
    private String rawText;

    @Column(columnDefinition = "JSON")
    private String structuredJson;

    private LocalDateTime submittedAt;
}
