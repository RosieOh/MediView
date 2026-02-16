package com.mediview.domain.intake.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IntakeFormRequest {

    @NotBlank
    private String rawText;

    private String structuredJson;
}
