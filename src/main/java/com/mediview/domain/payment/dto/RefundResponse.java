package com.mediview.domain.payment.dto;

import com.mediview.domain.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class RefundResponse {

    private Long id;
    private Long paymentId;
    private BigDecimal amount;
    private String reason;
    private PaymentStatus status;
    private LocalDateTime processedAt;
}
