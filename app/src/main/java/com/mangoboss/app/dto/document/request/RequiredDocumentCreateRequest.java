package com.mangoboss.app.dto.document.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import com.mangoboss.storage.document.DocumentType;

@Builder
public record RequiredDocumentCreateRequest(
        @NotNull
        DocumentType documentType,

        @NotNull
        Boolean isRequired
) {}