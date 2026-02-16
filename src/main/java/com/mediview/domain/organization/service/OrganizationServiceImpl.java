package com.mediview.domain.organization.service;

import com.mediview.domain.enums.UserRole;
import com.mediview.domain.exception.BadRequestException;
import com.mediview.domain.exception.NotFoundException;
import com.mediview.domain.organization.dto.*;
import com.mediview.domain.organization.entity.DoctorProfile;
import com.mediview.domain.organization.entity.Organization;
import com.mediview.domain.organization.repository.DoctorProfileRepository;
import com.mediview.domain.organization.repository.OrganizationRepository;
import com.mediview.domain.user.entity.User;
import com.mediview.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrganizationResponse createOrganization(OrganizationCreateRequest request) {
        if (organizationRepository.existsByBizNo(request.getBizNo())) {
            throw new BadRequestException("Business number already registered: " + request.getBizNo());
        }

        Organization org = Organization.builder()
                .name(request.getName())
                .type(request.getType())
                .bizNo(request.getBizNo())
                .address(request.getAddress())
                .phone(request.getPhone())
                .build();
        organizationRepository.save(org);

        return toResponse(org);
    }

    @Override
    public List<OrganizationResponse> getAllOrganizations() {
        return organizationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public DoctorProfileResponse inviteDoctor(Long organizationId, DoctorInviteRequest request) {
        Organization org = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization not found: " + organizationId));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.getEmail()));

        if (user.getRole() != UserRole.DOCTOR) {
            throw new BadRequestException("User is not a doctor");
        }

        if (doctorProfileRepository.findByUserId(user.getId()).isPresent()) {
            throw new BadRequestException("Doctor profile already exists for this user");
        }

        DoctorProfile dp = DoctorProfile.builder()
                .user(user)
                .organization(org)
                .licenseNoHash(request.getLicenseNo())
                .specialty(request.getSpecialty())
                .build();
        doctorProfileRepository.save(dp);

        return toDoctorResponse(dp);
    }

    private OrganizationResponse toResponse(Organization org) {
        return OrganizationResponse.builder()
                .id(org.getId())
                .name(org.getName())
                .type(org.getType())
                .bizNo(org.getBizNo())
                .status(org.getStatus())
                .address(org.getAddress())
                .phone(org.getPhone())
                .build();
    }

    private DoctorProfileResponse toDoctorResponse(DoctorProfile dp) {
        return DoctorProfileResponse.builder()
                .id(dp.getId())
                .userId(dp.getUser().getId())
                .email(dp.getUser().getEmail())
                .organizationId(dp.getOrganization().getId())
                .organizationName(dp.getOrganization().getName())
                .specialty(dp.getSpecialty())
                .verifiedAt(dp.getVerifiedAt())
                .build();
    }
}
