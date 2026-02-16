package com.mediview.domain.ai.entity;

import com.mediview.domain.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_ai_output")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiOutput extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private AiJob job;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "JSON")
    private String citationsJson;

    @Column(columnDefinition = "JSON")
    private String safetyFlagsJson;
}
