package com.mediview.domain.document.repository;

import com.mediview.domain.document.entity.DocumentFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentFileRepository extends JpaRepository<DocumentFile, Long> {

    List<DocumentFile> findByDocumentId(Long documentId);
}
