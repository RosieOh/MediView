package com.mediview.domain.document.repository;

import com.mediview.domain.document.entity.Document;
import com.mediview.domain.enums.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByAppointmentId(Long appointmentId);

    List<Document> findByStatus(DocumentStatus status);
}
