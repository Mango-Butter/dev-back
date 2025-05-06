package com.mangoboss.app.dto.contract.response;

import lombok.Builder;

@Builder
public record SignatureUploadResponse(
        String signatureKey
) {
    public static SignatureUploadResponse of(final String signatureKey) {
        return SignatureUploadResponse.builder()
                .signatureKey(signatureKey)
                .build();
    }
}