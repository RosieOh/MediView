package com.mediview.domain.user.controller;

import com.mediview.domain.response.ApiResponse;
import com.mediview.domain.user.dto.LoginRequest;
import com.mediview.domain.user.dto.LoginResponse;
import com.mediview.domain.user.dto.SignupRequest;
import com.mediview.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication APIs")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "Sign up")
    public ResponseEntity<ApiResponse<LoginResponse>> signup(@Valid @RequestBody SignupRequest request) {
        LoginResponse result = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<LoginResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Signup successful")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse result = userService.login(request);
        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Login successful")
                        .data(result)
                        .build()
        );
    }
}
