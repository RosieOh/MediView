package com.mediview.domain.auditlog.controller;

import com.mediview.domain.auditlog.dto.AuditLogResponse;
import com.mediview.domain.auditlog.service.AuditLogService;
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
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Log (Admin)", description = "Audit log APIs")
@SecurityRequirement(name = "BearerAuth")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @Operation(summary = "List all audit logs")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> list() {
        List<AuditLogResponse> result = auditLogService.getAllLogs();
        return ResponseEntity.ok(
                ApiResponse.<List<AuditLogResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Audit logs retrieved")
                        .data(result)
                        .build()
        );
    }
}
