package com.mediview.domain.intake.repository;

import com.mediview.domain.intake.entity.IntakeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntakeFileRepository extends JpaRepository<IntakeFile, Long> {

    List<IntakeFile> findByIntakeFormId(Long intakeFormId);
}
