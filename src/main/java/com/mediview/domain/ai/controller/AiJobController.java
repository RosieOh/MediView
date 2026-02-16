package com.mediview.domain.ai.controller;

import com.mediview.domain.ai.dto.AiJobRequest;
import com.mediview.domain.ai.dto.AiJobResponse;
import com.mediview.domain.ai.service.AiJobService;
import com.mediview.domain.enums.AiJobType;
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
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Job", description = "AI job management APIs")
@SecurityRequirement(name = "BearerAuth")
public class AiJobController {

    private final AiJobService aiJobService;

    @PostMapping("/intake-summary")
    @Operation(summary = "Create intake summary AI job")
    public ResponseEntity<ApiResponse<AiJobResponse>> intakeSummary(@Valid @RequestBody AiJobRequest request) {
        request.setType(AiJobType.INTAKE_SUMMARY);
        AiJobResponse result = aiJobService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<AiJobResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("AI intake summary job created")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/visit-summary")
    @Operation(summary = "Create visit summary AI job")
    public ResponseEntity<ApiResponse<AiJobResponse>> visitSummary(@Valid @RequestBody AiJobRequest request) {
        request.setType(AiJobType.VISIT_SUMMARY);
        AiJobResponse result = aiJobService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<AiJobResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("AI visit summary job created")
                        .data(result)
                        .build()
        );
    }

    @GetMapping("/jobs/{id}")
    @Operation(summary = "Get AI job status")
    public ResponseEntity<ApiResponse<AiJobResponse>> getJob(@PathVariable Long id) {
        AiJobResponse result = aiJobService.getJob(id);
        return ResponseEntity.ok(
                ApiResponse.<AiJobResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("AI job retrieved")
                        .data(result)
                        .build()
        );
    }
}
