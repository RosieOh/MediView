package com.mediview.domain.user.service;

import com.mediview.domain.user.dto.*;

public interface UserService {

    LoginResponse signup(SignupRequest request);

    LoginResponse login(LoginRequest request);

    UserProfileResponse getMyProfile(String email);

    UserProfileResponse updateMyProfile(String email, UserProfileUpdateRequest request);
}
