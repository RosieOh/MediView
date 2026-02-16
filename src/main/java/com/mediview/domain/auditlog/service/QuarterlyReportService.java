package com.mediview.domain.auditlog.service;

import com.mediview.domain.auditlog.dto.QuarterlyReportResponse;

import java.util.List;

public interface QuarterlyReportService {

    QuarterlyReportResponse generateReport(String period, Long generatedBy);

    List<QuarterlyReportResponse> getAllReports();
}
