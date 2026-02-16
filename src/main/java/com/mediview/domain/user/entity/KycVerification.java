package com.mediview.domain.user.entity;

import com.mediview.domain.audit.BaseEntity;
import com.mediview.domain.enums.KycStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_kyc_verification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycVerification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String requestId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private KycStatus status = KycStatus.PENDING;

    private String ciHash;

    private String diHash;

    private LocalDateTime verifiedAt;

    private LocalDateTime expiresAt;
}
