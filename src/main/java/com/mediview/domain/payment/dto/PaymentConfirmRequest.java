package com.mediview.domain.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentConfirmRequest {

    @NotNull
    private Long paymentId;

    @NotBlank
    private String pgTxId;
}
