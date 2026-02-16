package com.mediview.domain.payment.service;

import com.mediview.domain.payment.dto.SettlementResponse;

import java.util.List;

public interface SettlementService {

    List<SettlementResponse> generateSettlements(String period);

    List<SettlementResponse> getSettlementsByPeriod(String period);

    List<SettlementResponse> getSettlementsByOrganization(Long organizationId);
}
