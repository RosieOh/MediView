package com.mediview.domain.organization.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DoctorProfileResponse {

    private Long id;
    private Long userId;
    private String email;
    private String name;
    private Long organizationId;
    private String organizationName;
    private String specialty;
    private LocalDateTime verifiedAt;
}
