package com.mediview.domain.ai.entity;

import com.mediview.domain.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_rag_chunk")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RagChunk extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", nullable = false)
    private RagDocument document;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String chunkText;

    @Column(columnDefinition = "JSON")
    private String metaJson;
}
