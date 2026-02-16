package com.mediview.domain.user.controller;

import com.mediview.domain.response.ApiResponse;
import com.mediview.domain.user.dto.KycRequestDto;
import com.mediview.domain.user.dto.KycResponse;
import com.mediview.domain.user.dto.KycVerifyDto;
import com.mediview.domain.user.service.KycService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
@Tag(name = "KYC", description = "Identity verification APIs")
@SecurityRequirement(name = "BearerAuth")
public class KycController {

    private final KycService kycService;

    @PostMapping("/request")
    @Operation(summary = "Request identity verification")
    public ResponseEntity<ApiResponse<KycResponse>> request(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody KycRequestDto request) {
        KycResponse result = kycService.requestVerification(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<KycResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("KYC verification requested")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify identity")
    public ResponseEntity<ApiResponse<KycResponse>> verify(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody KycVerifyDto request) {
        KycResponse result = kycService.verify(userDetails.getUsername(), request);
        return ResponseEntity.ok(
                ApiResponse.<KycResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("KYC verification completed")
                        .data(result)
                        .build()
        );
    }
}
