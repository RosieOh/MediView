package com.mediview.domain.policy.service;

import com.mediview.domain.policy.dto.PolicyResponse;
import com.mediview.domain.policy.dto.PolicyUpdateRequest;

import java.util.List;

public interface PolicyService {

    PolicyResponse upsertPolicy(String key, PolicyUpdateRequest request);

    List<PolicyResponse> getAllPolicies();
}
