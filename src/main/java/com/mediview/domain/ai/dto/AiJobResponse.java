package com.mediview.domain.ai.dto;

import com.mediview.domain.enums.AiJobStatus;
import com.mediview.domain.enums.AiJobType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AiJobResponse {

    private Long id;
    private AiJobType type;
    private AiJobStatus status;
    private String inputRef;
    private String outputContent;
    private String citationsJson;
    private String safetyFlagsJson;
    private LocalDateTime createdAt;
}
