package com.mediview.domain.policy.repository;

import com.mediview.domain.enums.PolicyCategory;
import com.mediview.domain.policy.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    Optional<Policy> findByKey(String key);

    List<Policy> findByCategory(PolicyCategory category);
}
