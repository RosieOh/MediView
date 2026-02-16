package com.mediview.domain.ai.repository;

import com.mediview.domain.ai.entity.RagDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RagDocumentRepository extends JpaRepository<RagDocument, Long> {

    List<RagDocument> findBySource(String source);
}
