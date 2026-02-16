package com.mediview.domain.document.service;

import com.mediview.domain.document.dto.DocumentCreateRequest;
import com.mediview.domain.document.dto.DocumentResponse;
import com.mediview.domain.document.dto.DocumentUpdateRequest;

public interface DocumentService {

    DocumentResponse createDocument(DocumentCreateRequest request);

    DocumentResponse updateDocument(Long id, DocumentUpdateRequest request);

    DocumentResponse approveDocument(Long id);

    DocumentResponse getDocument(Long id);
}
