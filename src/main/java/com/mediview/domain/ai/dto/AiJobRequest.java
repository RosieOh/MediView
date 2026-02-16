package com.mediview.domain.ai.dto;

import com.mediview.domain.enums.AiJobType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AiJobRequest {

    @NotNull
    private AiJobType type;

    private String inputRef;
}
