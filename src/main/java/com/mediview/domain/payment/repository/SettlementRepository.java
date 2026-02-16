package com.mediview.domain.payment.repository;

import com.mediview.domain.payment.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    List<Settlement> findByOrganizationId(Long organizationId);

    List<Settlement> findByPeriod(String period);
}
