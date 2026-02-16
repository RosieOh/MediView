package com.mediview.domain.policy.dto;

import com.mediview.domain.enums.PolicyCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PolicyUpdateRequest {

    @NotNull
    private PolicyCategory category;

    private String configJson;

    private LocalDateTime activeFrom;

    private LocalDateTime activeTo;
}
