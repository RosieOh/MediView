package com.mediview.domain.organization.service;

import com.mediview.domain.organization.dto.DoctorProfileResponse;
import com.mediview.domain.organization.dto.DoctorProfileUpdateRequest;

public interface DoctorProfileService {

    DoctorProfileResponse getMyDoctorProfile(String email);

    DoctorProfileResponse updateMyDoctorProfile(String email, DoctorProfileUpdateRequest request);
}
