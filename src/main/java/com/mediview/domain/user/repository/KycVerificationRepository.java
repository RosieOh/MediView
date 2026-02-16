package com.mediview.domain.user.repository;

import com.mediview.domain.enums.KycStatus;
import com.mediview.domain.user.entity.KycVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KycVerificationRepository extends JpaRepository<KycVerification, Long> {

    Optional<KycVerification> findByRequestId(String requestId);

    List<KycVerification> findByUserId(Long userId);

    List<KycVerification> findByUserIdAndStatus(Long userId, KycStatus status);
}
