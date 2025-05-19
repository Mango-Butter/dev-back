package com.mangoboss.app.dto.document.response;

import com.mangoboss.storage.document.DocumentType;

import java.time.LocalDate;

public record MyDocumentStatusResponse(
        DocumentType documentType,
        boolean isSubmitted,
        LocalDate expiresAt,
        Long documentId
) {
    public static MyDocumentStatusResponse of(
            final DocumentType type,
            final boolean isSubmitted,
            final LocalDate expiresAt,
            final Long documentId
    ) {
        return new MyDocumentStatusResponse(type, isSubmitted, expiresAt, documentId);
    }
}
