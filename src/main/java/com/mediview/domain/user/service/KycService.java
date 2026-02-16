package com.mediview.domain.user.service;

import com.mediview.domain.user.dto.KycRequestDto;
import com.mediview.domain.user.dto.KycResponse;
import com.mediview.domain.user.dto.KycVerifyDto;

public interface KycService {

    KycResponse requestVerification(String email, KycRequestDto request);

    KycResponse verify(String email, KycVerifyDto request);
}
