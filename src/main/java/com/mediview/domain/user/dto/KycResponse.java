package com.mediview.domain.user.dto;

import com.mediview.domain.enums.KycStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class KycResponse {

    private Long id;
    private String requestId;
    private String method;
    private KycStatus status;
    private LocalDateTime verifiedAt;
    private LocalDateTime expiresAt;
}
