package com.mediview.domain.document.dto;

import com.mediview.domain.enums.DocumentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentCreateRequest {

    @NotNull
    private Long appointmentId;

    @NotNull
    private DocumentType type;

    private String content;
}
