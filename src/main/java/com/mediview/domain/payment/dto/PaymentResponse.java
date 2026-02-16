package com.mediview.domain.payment.dto;

import com.mediview.domain.enums.PaymentMethod;
import com.mediview.domain.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {

    private Long id;
    private Long appointmentId;
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private String pgTxId;
    private LocalDateTime paidAt;
}
