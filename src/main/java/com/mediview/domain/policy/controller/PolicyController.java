package com.mediview.domain.policy.controller;

import com.mediview.domain.policy.dto.PolicyResponse;
import com.mediview.domain.policy.dto.PolicyUpdateRequest;
import com.mediview.domain.policy.service.PolicyService;
import com.mediview.domain.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/policies")
@RequiredArgsConstructor
@Tag(name = "Policy (Admin)", description = "Policy management APIs")
@SecurityRequirement(name = "BearerAuth")
public class PolicyController {

    private final PolicyService policyService;

    @PutMapping("/{key}")
    @Operation(summary = "Create or update policy")
    public ResponseEntity<ApiResponse<PolicyResponse>> upsert(
            @PathVariable String key,
            @Valid @RequestBody PolicyUpdateRequest request) {
        PolicyResponse result = policyService.upsertPolicy(key, request);
        return ResponseEntity.ok(
                ApiResponse.<PolicyResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Policy updated")
                        .data(result)
                        .build()
        );
    }

    @GetMapping
    @Operation(summary = "List all policies")
    public ResponseEntity<ApiResponse<List<PolicyResponse>>> list() {
        List<PolicyResponse> result = policyService.getAllPolicies();
        return ResponseEntity.ok(
                ApiResponse.<List<PolicyResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Policies retrieved")
                        .data(result)
                        .build()
        );
    }
}
