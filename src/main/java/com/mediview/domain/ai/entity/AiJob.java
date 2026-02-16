package com.mediview.domain.ai.entity;

import com.mediview.domain.audit.BaseEntity;
import com.mediview.domain.enums.AiJobStatus;
import com.mediview.domain.enums.AiJobType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_ai_job")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiJob extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AiJobType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AiJobStatus status = AiJobStatus.PENDING;

    private String inputRef;

    @Column(columnDefinition = "TEXT")
    private String outputContent;

    @Column(columnDefinition = "JSON")
    private String citationsJson;

    @Column(columnDefinition = "JSON")
    private String safetyFlagsJson;
}
