package com.mediview.domain.consultation.entity;

import com.mediview.domain.appointment.entity.Appointment;
import com.mediview.domain.audit.BaseEntity;
import com.mediview.domain.enums.ConsultChannel;
import com.mediview.domain.enums.ConsultStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_consult_session")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultSession extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ConsultStatus status = ConsultStatus.WAITING;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private String webrtcRoomId;
}
