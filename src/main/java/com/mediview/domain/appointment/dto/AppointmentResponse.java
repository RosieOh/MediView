package com.mediview.domain.appointment.dto;

import com.mediview.domain.enums.AppointmentStatus;
import com.mediview.domain.enums.AppointmentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentResponse {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private Long organizationId;
    private AppointmentType type;
    private AppointmentStatus status;
    private LocalDateTime scheduledAt;
    private Integer queueOrder;
    private LocalDateTime createdAt;
}
