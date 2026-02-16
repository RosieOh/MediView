package com.mediview.domain.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SettlementResponse {

    private Long id;
    private Long organizationId;
    private String organizationName;
    private String period;
    private BigDecimal totalAmount;
    private BigDecimal platformFee;
    private String status;
    private LocalDateTime createdAt;
}
