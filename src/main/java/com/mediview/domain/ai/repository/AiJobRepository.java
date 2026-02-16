package com.mediview.domain.ai.repository;

import com.mediview.domain.ai.entity.AiJob;
import com.mediview.domain.enums.AiJobStatus;
import com.mediview.domain.enums.AiJobType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiJobRepository extends JpaRepository<AiJob, Long> {

    List<AiJob> findByType(AiJobType type);

    List<AiJob> findByStatus(AiJobStatus status);
}
