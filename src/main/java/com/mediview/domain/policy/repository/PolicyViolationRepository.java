package com.mediview.domain.policy.repository;

import com.mediview.domain.policy.entity.PolicyViolation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyViolationRepository extends JpaRepository<PolicyViolation, Long> {

    List<PolicyViolation> findByActorId(Long actorId);

    List<PolicyViolation> findByRuleKey(String ruleKey);
}
