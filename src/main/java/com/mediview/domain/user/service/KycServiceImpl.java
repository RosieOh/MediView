package com.mediview.domain.user.service;

import com.mediview.domain.enums.KycStatus;
import com.mediview.domain.exception.BadRequestException;
import com.mediview.domain.exception.NotFoundException;
import com.mediview.domain.user.dto.KycRequestDto;
import com.mediview.domain.user.dto.KycResponse;
import com.mediview.domain.user.dto.KycVerifyDto;
import com.mediview.domain.user.entity.KycVerification;
import com.mediview.domain.user.entity.User;
import com.mediview.domain.user.entity.UserProfile;
import com.mediview.domain.user.repository.KycVerificationRepository;
import com.mediview.domain.user.repository.UserProfileRepository;
import com.mediview.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KycServiceImpl implements KycService {

    private final KycVerificationRepository kycRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public KycResponse requestVerification(String email, KycRequestDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String requestId = UUID.randomUUID().toString();

        KycVerification kyc = KycVerification.builder()
                .user(user)
                .method(request.getMethod())
                .requestId(requestId)
                .status(KycStatus.PENDING)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();
        kycRepository.save(kyc);

        return toResponse(kyc);
    }

    @Override
    @Transactional
    public KycResponse verify(String email, KycVerifyDto request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        KycVerification kyc = kycRepository.findByRequestId(request.getRequestId())
                .orElseThrow(() -> new NotFoundException("KYC request not found"));

        if (kyc.getStatus() != KycStatus.PENDING) {
            throw new BadRequestException("KYC request is not in PENDING status");
        }

        if (kyc.getExpiresAt().isBefore(LocalDateTime.now())) {
            kyc.setStatus(KycStatus.EXPIRED);
            kycRepository.save(kyc);
            throw new BadRequestException("KYC request has expired");
        }

        // In production, verify against external KYC provider here
        // For MVP, we accept any verification code
        kyc.setStatus(KycStatus.VERIFIED);
        kyc.setVerifiedAt(LocalDateTime.now());
        kyc.setCiHash(UUID.randomUUID().toString());
        kyc.setDiHash(UUID.randomUUID().toString());
        kycRepository.save(kyc);

        // Update user profile with verification
        UserProfile profile = userProfileRepository.findByUserId(user.getId()).orElse(null);
        if (profile != null) {
            profile.setCiHash(kyc.getCiHash());
            profile.setVerifiedAt(kyc.getVerifiedAt());
            userProfileRepository.save(profile);
        }

        return toResponse(kyc);
    }

    private KycResponse toResponse(KycVerification kyc) {
        return KycResponse.builder()
                .id(kyc.getId())
                .requestId(kyc.getRequestId())
                .method(kyc.getMethod())
                .status(kyc.getStatus())
                .verifiedAt(kyc.getVerifiedAt())
                .expiresAt(kyc.getExpiresAt())
                .build();
    }
}
