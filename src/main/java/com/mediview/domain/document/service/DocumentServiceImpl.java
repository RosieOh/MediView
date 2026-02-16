package com.mediview.domain.document.service;

import com.mediview.domain.appointment.entity.Appointment;
import com.mediview.domain.appointment.repository.AppointmentRepository;
import com.mediview.domain.document.dto.DocumentCreateRequest;
import com.mediview.domain.document.dto.DocumentResponse;
import com.mediview.domain.document.dto.DocumentUpdateRequest;
import com.mediview.domain.document.entity.Document;
import com.mediview.domain.document.repository.DocumentRepository;
import com.mediview.domain.enums.DocumentStatus;
import com.mediview.domain.exception.BadRequestException;
import com.mediview.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public DocumentResponse createDocument(DocumentCreateRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        Document doc = Document.builder()
                .appointment(appointment)
                .type(request.getType())
                .content(request.getContent())
                .status(DocumentStatus.DRAFT)
                .build();
        documentRepository.save(doc);

        return toResponse(doc);
    }

    @Override
    @Transactional
    public DocumentResponse updateDocument(Long id, DocumentUpdateRequest request) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Document not found: " + id));

        if (doc.getStatus() == DocumentStatus.ISSUED) {
            throw new BadRequestException("Cannot update issued document");
        }

        if (request.getContent() != null) {
            doc.setContent(request.getContent());
        }
        documentRepository.save(doc);

        return toResponse(doc);
    }

    @Override
    @Transactional
    public DocumentResponse approveDocument(Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Document not found: " + id));

        if (doc.getStatus() != DocumentStatus.DRAFT && doc.getStatus() != DocumentStatus.APPROVED) {
            throw new BadRequestException("Document cannot be approved in current status");
        }

        doc.setStatus(DocumentStatus.ISSUED);
        doc.setIssuedAt(LocalDateTime.now());
        documentRepository.save(doc);

        return toResponse(doc);
    }

    @Override
    public DocumentResponse getDocument(Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Document not found: " + id));
        return toResponse(doc);
    }

    private DocumentResponse toResponse(Document doc) {
        return DocumentResponse.builder()
                .id(doc.getId())
                .appointmentId(doc.getAppointment().getId())
                .type(doc.getType())
                .content(doc.getContent())
                .status(doc.getStatus())
                .issuedAt(doc.getIssuedAt())
                .createdAt(doc.getCreatedAt())
                .build();
    }
}
