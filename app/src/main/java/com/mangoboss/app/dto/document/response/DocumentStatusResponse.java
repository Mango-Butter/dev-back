package com.mangoboss.app.dto.document.response;

import com.mangoboss.storage.document.DocumentType;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record DocumentStatusResponse(
        DocumentType documentType,
        boolean isSubmitted,
        LocalDate expiresAt,
        Long documentId
) {
    public static DocumentStatusResponse of(final DocumentType documentType, final boolean isSubmitted,
                                            final LocalDate expiresAt, final Long documentId) {
        return DocumentStatusResponse.builder()
                .documentType(documentType)
                .isSubmitted(isSubmitted)
                .expiresAt(expiresAt)
                .documentId(documentId)
                .build();
    }
}
