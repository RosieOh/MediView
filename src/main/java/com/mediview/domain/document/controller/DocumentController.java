package com.mediview.domain.document.controller;

import com.mediview.domain.document.dto.DocumentCreateRequest;
import com.mediview.domain.document.dto.DocumentResponse;
import com.mediview.domain.document.dto.DocumentUpdateRequest;
import com.mediview.domain.document.service.DocumentService;
import com.mediview.domain.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Tag(name = "Document", description = "Document APIs")
@SecurityRequirement(name = "BearerAuth")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    @Operation(summary = "Create document (DRAFT)")
    public ResponseEntity<ApiResponse<DocumentResponse>> create(
            @Valid @RequestBody DocumentCreateRequest request) {
        DocumentResponse result = documentService.createDocument(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<DocumentResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Document created as DRAFT")
                        .data(result)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update document content")
    public ResponseEntity<ApiResponse<DocumentResponse>> update(
            @PathVariable Long id,
            @RequestBody DocumentUpdateRequest request) {
        DocumentResponse result = documentService.updateDocument(id, request);
        return ResponseEntity.ok(
                ApiResponse.<DocumentResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Document updated")
                        .data(result)
                        .build()
        );
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve and issue document")
    public ResponseEntity<ApiResponse<DocumentResponse>> approve(@PathVariable Long id) {
        DocumentResponse result = documentService.approveDocument(id);
        return ResponseEntity.ok(
                ApiResponse.<DocumentResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Document approved and issued")
                        .data(result)
                        .build()
        );
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Download document")
    public ResponseEntity<ApiResponse<DocumentResponse>> download(@PathVariable Long id) {
        DocumentResponse result = documentService.getDocument(id);
        return ResponseEntity.ok(
                ApiResponse.<DocumentResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Document retrieved")
                        .data(result)
                        .build()
        );
    }
}
