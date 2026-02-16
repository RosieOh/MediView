package com.mediview.domain.organization.dto;

import com.mediview.domain.enums.OrganizationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrganizationCreateRequest {

    @NotBlank
    private String name;

    @NotNull
    private OrganizationType type;

    @NotBlank
    private String bizNo;

    private String address;

    private String phone;
}
