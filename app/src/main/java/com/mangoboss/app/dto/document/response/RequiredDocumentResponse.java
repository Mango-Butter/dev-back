package com.mangoboss.app.dto.document.response;

import com.mangoboss.storage.document.DocumentType;
import com.mangoboss.storage.document.RequiredDocumentEntity;
import lombok.Builder;

@Builder
public record RequiredDocumentResponse(
        DocumentType documentType,
        boolean isRequired
) {
    public static RequiredDocumentResponse fromEntity(final RequiredDocumentEntity entity) {
        return RequiredDocumentResponse.builder()
                .documentType(entity.getDocumentType())
                .isRequired(entity.isRequired())
                .build();
    }
}