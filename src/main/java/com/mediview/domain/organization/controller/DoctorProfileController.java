package com.mediview.domain.organization.controller;

import com.mediview.domain.organization.dto.DoctorProfileResponse;
import com.mediview.domain.organization.dto.DoctorProfileUpdateRequest;
import com.mediview.domain.organization.service.DoctorProfileService;
import com.mediview.domain.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor", description = "Doctor profile APIs")
@SecurityRequirement(name = "BearerAuth")
public class DoctorProfileController {

    private final DoctorProfileService doctorProfileService;

    @GetMapping("/me")
    @Operation(summary = "Get my doctor profile")
    public ResponseEntity<ApiResponse<DoctorProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        DoctorProfileResponse result = doctorProfileService.getMyDoctorProfile(userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.<DoctorProfileResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Doctor profile retrieved")
                        .data(result)
                        .build()
        );
    }

    @PutMapping("/me")
    @Operation(summary = "Update my doctor profile")
    public ResponseEntity<ApiResponse<DoctorProfileResponse>> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DoctorProfileUpdateRequest request) {
        DoctorProfileResponse result = doctorProfileService.updateMyDoctorProfile(
                userDetails.getUsername(), request);
        return ResponseEntity.ok(
                ApiResponse.<DoctorProfileResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Doctor profile updated")
                        .data(result)
                        .build()
        );
    }
}
