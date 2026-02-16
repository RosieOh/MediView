package com.mediview.domain.appointment.dto;

import com.mediview.domain.enums.AppointmentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentCreateRequest {

    @NotNull
    private Long doctorId;

    @NotNull
    private Long organizationId;

    @NotNull
    private AppointmentType type;

    private LocalDateTime scheduledAt;
}
