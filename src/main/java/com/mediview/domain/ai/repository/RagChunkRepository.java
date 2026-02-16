package com.mediview.domain.ai.repository;

import com.mediview.domain.ai.entity.RagChunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RagChunkRepository extends JpaRepository<RagChunk, Long> {

    List<RagChunk> findByDocumentId(Long documentId);
}
