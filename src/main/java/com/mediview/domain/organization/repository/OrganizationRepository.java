package com.mediview.domain.organization.repository;

import com.mediview.domain.enums.OrganizationStatus;
import com.mediview.domain.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByBizNo(String bizNo);

    List<Organization> findByStatus(OrganizationStatus status);

    boolean existsByBizNo(String bizNo);
}
