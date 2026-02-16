package com.mediview.domain.organization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DoctorInviteRequest {

    @Email
    @NotBlank
    private String email;

    private String licenseNo;

    private String specialty;
}
