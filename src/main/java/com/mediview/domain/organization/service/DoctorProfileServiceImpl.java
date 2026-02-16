package com.mediview.domain.organization.service;

import com.mediview.domain.exception.NotFoundException;
import com.mediview.domain.organization.dto.DoctorProfileResponse;
import com.mediview.domain.organization.dto.DoctorProfileUpdateRequest;
import com.mediview.domain.organization.entity.DoctorProfile;
import com.mediview.domain.organization.repository.DoctorProfileRepository;
import com.mediview.domain.user.entity.User;
import com.mediview.domain.user.entity.UserProfile;
import com.mediview.domain.user.repository.UserProfileRepository;
import com.mediview.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorProfileServiceImpl implements DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public DoctorProfileResponse getMyDoctorProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));

        DoctorProfile dp = doctorProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Doctor profile not found"));

        return toResponse(dp);
    }

    @Override
    @Transactional
    public DoctorProfileResponse updateMyDoctorProfile(String email, DoctorProfileUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));

        DoctorProfile dp = doctorProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Doctor profile not found"));

        if (request.getSpecialty() != null) {
            dp.setSpecialty(request.getSpecialty());
        }
        doctorProfileRepository.save(dp);

        return toResponse(dp);
    }

    private DoctorProfileResponse toResponse(DoctorProfile dp) {
        UserProfile userProfile = userProfileRepository.findByUserId(dp.getUser().getId()).orElse(null);
        return DoctorProfileResponse.builder()
                .id(dp.getId())
                .userId(dp.getUser().getId())
                .email(dp.getUser().getEmail())
                .name(userProfile != null ? userProfile.getName() : null)
                .organizationId(dp.getOrganization().getId())
                .organizationName(dp.getOrganization().getName())
                .specialty(dp.getSpecialty())
                .verifiedAt(dp.getVerifiedAt())
                .build();
    }
}
