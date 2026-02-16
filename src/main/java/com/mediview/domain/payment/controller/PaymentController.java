package com.mediview.domain.payment.controller;

import com.mediview.domain.payment.dto.*;
import com.mediview.domain.payment.service.PaymentService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment APIs")
@SecurityRequirement(name = "BearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payments/prepare")
    @Operation(summary = "Prepare payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> prepare(
            @Valid @RequestBody PaymentPrepareRequest request) {
        PaymentResponse result = paymentService.preparePayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<PaymentResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Payment prepared")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/payments/confirm")
    @Operation(summary = "Confirm payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> confirm(
            @Valid @RequestBody PaymentConfirmRequest request) {
        PaymentResponse result = paymentService.confirmPayment(request);
        return ResponseEntity.ok(
                ApiResponse.<PaymentResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Payment confirmed")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/refunds")
    @Operation(summary = "Create refund")
    public ResponseEntity<ApiResponse<RefundResponse>> refund(
            @Valid @RequestBody RefundRequest request) {
        RefundResponse result = paymentService.createRefund(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<RefundResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Refund created")
                        .data(result)
                        .build()
        );
    }
}
