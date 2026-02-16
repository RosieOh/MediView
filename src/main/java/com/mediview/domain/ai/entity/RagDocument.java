package com.mediview.domain.ai.entity;

import com.mediview.domain.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_rag_document")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RagDocument extends BaseEntity {

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String title;

    private String version;

    private LocalDate effectiveDate;

    private String objectKey;

    private String hash;
}
