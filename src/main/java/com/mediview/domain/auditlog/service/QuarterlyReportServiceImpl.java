package com.mediview.domain.auditlog.service;

import com.mediview.domain.auditlog.dto.QuarterlyReportResponse;
import com.mediview.domain.auditlog.entity.QuarterlyReport;
import com.mediview.domain.auditlog.repository.QuarterlyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuarterlyReportServiceImpl implements QuarterlyReportService {

    private final QuarterlyReportRepository reportRepository;

    @Override
    @Transactional
    public QuarterlyReportResponse generateReport(String period, Long generatedBy) {
        // In production, this would aggregate data and generate a file
        String fileKey = "reports/quarterly/" + period + "/" + UUID.randomUUID() + ".pdf";

        QuarterlyReport report = QuarterlyReport.builder()
                .period(period)
                .generatedBy(generatedBy)
                .fileKey(fileKey)
                .build();
        reportRepository.save(report);

        return toResponse(report);
    }

    @Override
    public List<QuarterlyReportResponse> getAllReports() {
        return reportRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private QuarterlyReportResponse toResponse(QuarterlyReport r) {
        return QuarterlyReportResponse.builder()
                .id(r.getId())
                .period(r.getPeriod())
                .generatedBy(r.getGeneratedBy())
                .fileKey(r.getFileKey())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
