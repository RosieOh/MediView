package com.mediview.domain.organization.controller;

import com.mediview.domain.organization.dto.*;
import com.mediview.domain.organization.service.OrganizationService;
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
@RequestMapping("/api/admin/organizations")
@RequiredArgsConstructor
@Tag(name = "Organization (Admin)", description = "Organization management APIs")
@SecurityRequirement(name = "BearerAuth")
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    @Operation(summary = "Create organization")
    public ResponseEntity<ApiResponse<OrganizationResponse>> create(
            @Valid @RequestBody OrganizationCreateRequest request) {
        OrganizationResponse result = organizationService.createOrganization(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<OrganizationResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Organization created")
                        .data(result)
                        .build()
        );
    }

    @GetMapping
    @Operation(summary = "List all organizations")
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> list() {
        List<OrganizationResponse> result = organizationService.getAllOrganizations();
        return ResponseEntity.ok(
                ApiResponse.<List<OrganizationResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Organizations retrieved")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/{id}/doctors/invite")
    @Operation(summary = "Invite doctor to organization")
    public ResponseEntity<ApiResponse<DoctorProfileResponse>> inviteDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorInviteRequest request) {
        DoctorProfileResponse result = organizationService.inviteDoctor(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<DoctorProfileResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Doctor invited")
                        .data(result)
                        .build()
        );
    }
}
