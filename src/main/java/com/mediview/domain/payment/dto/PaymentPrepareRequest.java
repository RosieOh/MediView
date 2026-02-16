package com.mediview.domain.payment.dto;

import com.mediview.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentPrepareRequest {

    @NotNull
    private Long appointmentId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private PaymentMethod method;
}
