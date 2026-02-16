package com.mediview.domain.intake.controller;

import com.mediview.domain.intake.dto.IntakeFormRequest;
import com.mediview.domain.intake.dto.IntakeFormResponse;
import com.mediview.domain.intake.service.IntakeService;
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
@RequestMapping("/api/appointments/{appointmentId}/intakes")
@RequiredArgsConstructor
@Tag(name = "Intake", description = "Intake form APIs")
@SecurityRequirement(name = "BearerAuth")
public class IntakeController {

    private final IntakeService intakeService;

    @PostMapping
    @Operation(summary = "Submit intake form")
    public ResponseEntity<ApiResponse<IntakeFormResponse>> create(
            @PathVariable Long appointmentId,
            @Valid @RequestBody IntakeFormRequest request) {
        IntakeFormResponse result = intakeService.createIntakeForm(appointmentId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<IntakeFormResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Intake form submitted")
                        .data(result)
                        .build()
        );
    }

    @GetMapping
    @Operation(summary = "Get intake forms for appointment")
    public ResponseEntity<ApiResponse<List<IntakeFormResponse>>> list(@PathVariable Long appointmentId) {
        List<IntakeFormResponse> result = intakeService.getIntakeForms(appointmentId);
        return ResponseEntity.ok(
                ApiResponse.<List<IntakeFormResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Intake forms retrieved")
                        .data(result)
                        .build()
        );
    }
}
