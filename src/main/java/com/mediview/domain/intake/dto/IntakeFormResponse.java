package com.mediview.domain.intake.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IntakeFormResponse {

    private Long id;
    private Long appointmentId;
    private String rawText;
    private String structuredJson;
    private LocalDateTime submittedAt;
}
