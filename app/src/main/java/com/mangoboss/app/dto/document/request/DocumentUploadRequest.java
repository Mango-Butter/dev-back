package com.mangoboss.app.dto.document.request;

import com.mangoboss.storage.document.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DocumentUploadRequest(
        @NotBlank
        String documentData,

        @NotNull
        DocumentType documentType,

        LocalDate expiresAt
) {}