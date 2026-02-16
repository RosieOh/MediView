package com.mediview.domain.organization.service;

import com.mediview.domain.organization.dto.DoctorInviteRequest;
import com.mediview.domain.organization.dto.DoctorProfileResponse;
import com.mediview.domain.organization.dto.OrganizationCreateRequest;
import com.mediview.domain.organization.dto.OrganizationResponse;

import java.util.List;

public interface OrganizationService {

    OrganizationResponse createOrganization(OrganizationCreateRequest request);

    List<OrganizationResponse> getAllOrganizations();

    DoctorProfileResponse inviteDoctor(Long organizationId, DoctorInviteRequest request);
}
