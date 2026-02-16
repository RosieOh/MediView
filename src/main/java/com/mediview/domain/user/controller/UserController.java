package com.mediview.domain.user.controller;

import com.mediview.domain.response.ApiResponse;
import com.mediview.domain.user.dto.UserProfileResponse;
import com.mediview.domain.user.dto.UserProfileUpdateRequest;
import com.mediview.domain.user.service.UserService;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User profile APIs")
@SecurityRequirement(name = "BearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get my profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserProfileResponse result = userService.getMyProfile(userDetails.getUsername());
        return ResponseEntity.ok(
                ApiResponse.<UserProfileResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Profile retrieved")
                        .data(result)
                        .build()
        );
    }

    @PutMapping("/me")
    @Operation(summary = "Update my profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserProfileUpdateRequest request) {
        UserProfileResponse result = userService.updateMyProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(
                ApiResponse.<UserProfileResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Profile updated")
                        .data(result)
                        .build()
        );
    }
}
