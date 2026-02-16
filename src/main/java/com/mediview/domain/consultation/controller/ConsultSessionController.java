package com.mediview.domain.consultation.controller;

import com.mediview.domain.consultation.dto.ConsultSessionCreateRequest;
import com.mediview.domain.consultation.dto.ConsultSessionResponse;
import com.mediview.domain.consultation.service.ConsultSessionService;
import com.mediview.domain.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
@Tag(name = "Consultation", description = "Consultation session APIs")
@SecurityRequirement(name = "BearerAuth")
public class ConsultSessionController {

    private final ConsultSessionService consultSessionService;

    @PostMapping
    @Operation(summary = "Create consultation session")
    public ResponseEntity<ApiResponse<ConsultSessionResponse>> create(
            @Valid @RequestBody ConsultSessionCreateRequest request) {
        ConsultSessionResponse result = consultSessionService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ConsultSessionResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Session created")
                        .data(result)
                        .build()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get session detail")
    public ResponseEntity<ApiResponse<ConsultSessionResponse>> get(@PathVariable Long id) {
        ConsultSessionResponse result = consultSessionService.getSession(id);
        return ResponseEntity.ok(
                ApiResponse.<ConsultSessionResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Session retrieved")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/{id}/end")
    @Operation(summary = "End consultation session")
    public ResponseEntity<ApiResponse<ConsultSessionResponse>> end(@PathVariable Long id) {
        ConsultSessionResponse result = consultSessionService.endSession(id);
        return ResponseEntity.ok(
                ApiResponse.<ConsultSessionResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Session ended")
                        .data(result)
                        .build()
        );
    }
}
