package com.mediview.domain.auditlog.repository;

import com.mediview.domain.auditlog.entity.QuarterlyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuarterlyReportRepository extends JpaRepository<QuarterlyReport, Long> {

    List<QuarterlyReport> findByPeriod(String period);
}
