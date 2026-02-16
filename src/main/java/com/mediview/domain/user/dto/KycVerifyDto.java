package com.mediview.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KycVerifyDto {

    @NotBlank
    private String requestId;

    @NotBlank
    private String verificationCode;
}
