package com.mediview.domain.policy.service;

import com.mediview.domain.policy.dto.PolicyResponse;
import com.mediview.domain.policy.dto.PolicyUpdateRequest;
import com.mediview.domain.policy.entity.Policy;
import com.mediview.domain.policy.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;

    @Override
    @Transactional
    public PolicyResponse upsertPolicy(String key, PolicyUpdateRequest request) {
        Policy policy = policyRepository.findByKey(key)
                .orElseGet(() -> Policy.builder().key(key).version(0).build());

        policy.setCategory(request.getCategory());
        policy.setConfigJson(request.getConfigJson());
        policy.setActiveFrom(request.getActiveFrom());
        policy.setActiveTo(request.getActiveTo());
        policy.setVersion(policy.getVersion() == null ? 1 : policy.getVersion() + 1);
        policyRepository.save(policy);

        return toResponse(policy);
    }

    @Override
    public List<PolicyResponse> getAllPolicies() {
        return policyRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private PolicyResponse toResponse(Policy p) {
        return PolicyResponse.builder()
                .id(p.getId())
                .key(p.getKey())
                .category(p.getCategory())
                .version(p.getVersion())
                .configJson(p.getConfigJson())
                .activeFrom(p.getActiveFrom())
                .activeTo(p.getActiveTo())
                .build();
    }
}
