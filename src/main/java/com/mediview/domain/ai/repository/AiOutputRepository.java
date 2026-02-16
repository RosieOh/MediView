package com.mediview.domain.ai.repository;

import com.mediview.domain.ai.entity.AiOutput;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiOutputRepository extends JpaRepository<AiOutput, Long> {

    List<AiOutput> findByJobId(Long jobId);
}
