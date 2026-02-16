package com.mediview.domain.document.dto;

import com.mediview.domain.enums.DocumentStatus;
import com.mediview.domain.enums.DocumentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentResponse {

    private Long id;
    private Long appointmentId;
    private DocumentType type;
    private String content;
    private DocumentStatus status;
    private LocalDateTime issuedAt;
    private LocalDateTime createdAt;
}
