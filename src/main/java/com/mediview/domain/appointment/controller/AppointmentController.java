package com.mediview.domain.appointment.controller;

import com.mediview.domain.appointment.dto.AppointmentCreateRequest;
import com.mediview.domain.appointment.dto.AppointmentResponse;
import com.mediview.domain.appointment.service.AppointmentService;
import com.mediview.domain.enums.AppointmentStatus;
import com.mediview.domain.response.ApiResponse;
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

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment", description = "Appointment APIs")
@SecurityRequirement(name = "BearerAuth")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Create appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AppointmentCreateRequest request) {
        AppointmentResponse result = appointmentService.createAppointment(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<AppointmentResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Appointment created")
                        .data(result)
                        .build()
        );
    }

    @GetMapping
    @Operation(summary = "List appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> list(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) AppointmentStatus status) {
        List<AppointmentResponse> result = appointmentService.getAppointments(userDetails.getUsername(), status);
        return ResponseEntity.ok(
                ApiResponse.<List<AppointmentResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Appointments retrieved")
                        .data(result)
                        .build()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment detail")
    public ResponseEntity<ApiResponse<AppointmentResponse>> get(@PathVariable Long id) {
        AppointmentResponse result = appointmentService.getAppointment(id);
        return ResponseEntity.ok(
                ApiResponse.<AppointmentResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Appointment retrieved")
                        .data(result)
                        .build()
        );
    }
}
