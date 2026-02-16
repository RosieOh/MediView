package com.mediview.domain.ai.service;

import com.mediview.domain.ai.dto.AiJobRequest;
import com.mediview.domain.ai.dto.AiJobResponse;
import com.mediview.domain.ai.entity.AiJob;
import com.mediview.domain.ai.repository.AiJobRepository;
import com.mediview.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiJobServiceImpl implements AiJobService {

    private final AiJobRepository aiJobRepository;

    @Override
    @Transactional
    public AiJobResponse createJob(AiJobRequest request) {
        AiJob job = AiJob.builder()
                .type(request.getType())
                .inputRef(request.getInputRef())
                .build();
        aiJobRepository.save(job);

        return toResponse(job);
    }

    @Override
    public AiJobResponse getJob(Long id) {
        AiJob job = aiJobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("AI Job not found: " + id));
        return toResponse(job);
    }

    private AiJobResponse toResponse(AiJob job) {
        return AiJobResponse.builder()
                .id(job.getId())
                .type(job.getType())
                .status(job.getStatus())
                .inputRef(job.getInputRef())
                .outputContent(job.getOutputContent())
                .citationsJson(job.getCitationsJson())
                .safetyFlagsJson(job.getSafetyFlagsJson())
                .createdAt(job.getCreatedAt())
                .build();
    }
}
