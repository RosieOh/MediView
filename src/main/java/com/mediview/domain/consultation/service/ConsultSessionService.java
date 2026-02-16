package com.mediview.domain.consultation.service;

import com.mediview.domain.consultation.dto.ConsultSessionCreateRequest;
import com.mediview.domain.consultation.dto.ConsultSessionResponse;

public interface ConsultSessionService {

    ConsultSessionResponse createSession(ConsultSessionCreateRequest request);

    ConsultSessionResponse getSession(Long id);

    ConsultSessionResponse endSession(Long id);
}
