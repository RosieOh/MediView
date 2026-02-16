package com.mediview.domain.payment.service;

import com.mediview.domain.enums.PaymentStatus;
import com.mediview.domain.organization.entity.Organization;
import com.mediview.domain.organization.repository.OrganizationRepository;
import com.mediview.domain.payment.dto.SettlementResponse;
import com.mediview.domain.payment.entity.Payment;
import com.mediview.domain.payment.entity.Settlement;
import com.mediview.domain.payment.repository.PaymentRepository;
import com.mediview.domain.payment.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementServiceImpl implements SettlementService {

    private static final BigDecimal PLATFORM_FEE_RATE = new BigDecimal("0.05");

    private final SettlementRepository settlementRepository;
    private final PaymentRepository paymentRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public List<SettlementResponse> generateSettlements(String period) {
        List<Payment> paidPayments = paymentRepository.findByStatus(PaymentStatus.PAID);

        Map<Long, List<Payment>> byOrg = paidPayments.stream()
                .collect(Collectors.groupingBy(p -> p.getAppointment().getOrganization().getId()));

        List<Settlement> settlements = new ArrayList<>();
        for (Map.Entry<Long, List<Payment>> entry : byOrg.entrySet()) {
            Organization org = organizationRepository.findById(entry.getKey()).orElse(null);
            if (org == null) continue;

            BigDecimal total = entry.getValue().stream()
                    .map(Payment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal fee = total.multiply(PLATFORM_FEE_RATE).setScale(2, RoundingMode.HALF_UP);

            Settlement settlement = Settlement.builder()
                    .organization(org)
                    .period(period)
                    .totalAmount(total)
                    .platformFee(fee)
                    .status("GENERATED")
                    .build();
            settlements.add(settlement);
        }

        settlementRepository.saveAll(settlements);
        return settlements.stream().map(this::toResponse).toList();
    }

    @Override
    public List<SettlementResponse> getSettlementsByPeriod(String period) {
        return settlementRepository.findByPeriod(period).stream()
                .map(this::toResponse).toList();
    }

    @Override
    public List<SettlementResponse> getSettlementsByOrganization(Long organizationId) {
        return settlementRepository.findByOrganizationId(organizationId).stream()
                .map(this::toResponse).toList();
    }

    private SettlementResponse toResponse(Settlement s) {
        return SettlementResponse.builder()
                .id(s.getId())
                .organizationId(s.getOrganization().getId())
                .organizationName(s.getOrganization().getName())
                .period(s.getPeriod())
                .totalAmount(s.getTotalAmount())
                .platformFee(s.getPlatformFee())
                .status(s.getStatus())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
