package com.mangoboss.app.dto.contract.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SignatureUploadRequest(
        @NotBlank
        String signatureData // "Base64(IV + 암호문)"
) {}