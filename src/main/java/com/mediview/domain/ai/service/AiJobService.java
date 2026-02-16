package com.mediview.domain.ai.service;

import com.mediview.domain.ai.dto.AiJobRequest;
import com.mediview.domain.ai.dto.AiJobResponse;

public interface AiJobService {

    AiJobResponse createJob(AiJobRequest request);

    AiJobResponse getJob(Long id);
}
