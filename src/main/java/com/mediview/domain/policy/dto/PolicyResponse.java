package com.mediview.domain.policy.dto;

import com.mediview.domain.enums.PolicyCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PolicyResponse {

    private Long id;
    private String key;
    private PolicyCategory category;
    private Integer version;
    private String configJson;
    private LocalDateTime activeFrom;
    private LocalDateTime activeTo;
}
