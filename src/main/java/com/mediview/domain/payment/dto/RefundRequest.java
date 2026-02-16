package com.mediview.domain.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundRequest {

    @NotNull
    private Long paymentId;

    @NotNull
    private BigDecimal amount;

    private String reason;
}
