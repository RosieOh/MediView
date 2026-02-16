package com.mediview.domain.payment.controller;

import com.mediview.domain.payment.dto.SettlementResponse;
import com.mediview.domain.payment.service.SettlementService;
import com.mediview.domain.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/settlements")
@RequiredArgsConstructor
@Tag(name = "Settlement (Admin)", description = "Settlement APIs")
@SecurityRequirement(name = "BearerAuth")
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping("/generate")
    @Operation(summary = "Generate settlements for a period")
    public ResponseEntity<ApiResponse<List<SettlementResponse>>> generate(
            @RequestParam String period) {
        List<SettlementResponse> result = settlementService.generateSettlements(period);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<List<SettlementResponse>>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Settlements generated")
                        .data(result)
                        .build()
        );
    }

    @GetMapping
    @Operation(summary = "Get settlements by period")
    public ResponseEntity<ApiResponse<List<SettlementResponse>>> getByPeriod(
            @RequestParam String period) {
        List<SettlementResponse> result = settlementService.getSettlementsByPeriod(period);
        return ResponseEntity.ok(
                ApiResponse.<List<SettlementResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Settlements retrieved")
                        .data(result)
                        .build()
        );
    }
}
