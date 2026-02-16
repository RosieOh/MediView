package com.mediview.domain.organization.dto;

import com.mediview.domain.enums.OrganizationStatus;
import com.mediview.domain.enums.OrganizationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationResponse {

    private Long id;
    private String name;
    private OrganizationType type;
    private String bizNo;
    private OrganizationStatus status;
    private String address;
    private String phone;
}
