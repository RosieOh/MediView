package com.mediview.domain.auditlog.controller;

import com.mediview.domain.auditlog.dto.QuarterlyReportResponse;
import com.mediview.domain.auditlog.service.QuarterlyReportService;
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
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
@Tag(name = "Quarterly Report (Admin)", description = "Quarterly report APIs")
@SecurityRequirement(name = "BearerAuth")
public class QuarterlyReportController {

    private final QuarterlyReportService reportService;

    @PostMapping("/quarterly")
    @Operation(summary = "Generate quarterly report")
    public ResponseEntity<ApiResponse<QuarterlyReportResponse>> generate(
            @RequestParam String period,
            @RequestParam Long generatedBy) {
        QuarterlyReportResponse result = reportService.generateReport(period, generatedBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<QuarterlyReportResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Quarterly report generated")
                        .data(result)
                        .build()
        );
    }

    @GetMapping("/quarterly")
    @Operation(summary = "List quarterly reports")
    public ResponseEntity<ApiResponse<List<QuarterlyReportResponse>>> list() {
        List<QuarterlyReportResponse> result = reportService.getAllReports();
        return ResponseEntity.ok(
                ApiResponse.<List<QuarterlyReportResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Reports retrieved")
                        .data(result)
                        .build()
        );
    }
}
