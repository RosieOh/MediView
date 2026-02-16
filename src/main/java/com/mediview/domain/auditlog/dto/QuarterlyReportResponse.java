package com.mediview.domain.auditlog.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuarterlyReportResponse {

    private Long id;
    private String period;
    private Long generatedBy;
    private String fileKey;
    private LocalDateTime createdAt;
}
