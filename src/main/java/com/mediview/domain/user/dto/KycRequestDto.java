package com.mediview.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KycRequestDto {

    @NotBlank
    private String method;

    private String phone;

    private String name;

    private String birthdate;
}
