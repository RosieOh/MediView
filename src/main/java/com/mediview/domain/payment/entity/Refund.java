package com.mediview.domain.payment.entity;

import com.mediview.domain.audit.BaseEntity;
import com.mediview.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_refund")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Refund extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    private LocalDateTime processedAt;
}
