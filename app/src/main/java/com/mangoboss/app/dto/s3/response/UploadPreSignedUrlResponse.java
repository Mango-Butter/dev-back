package com.mangoboss.app.dto.s3.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UploadPreSignedUrlResponse(
        String uploadUrl,
        String publicUrl,
        LocalDateTime expiresAt
) {
    public static UploadPreSignedUrlResponse of(
            final String uploadUrl,
            final String publicUrl,
            final LocalDateTime expiresAt

    ) {
        return UploadPreSignedUrlResponse.builder()
                .uploadUrl(uploadUrl)
                .publicUrl(publicUrl)
                .expiresAt(expiresAt)
                .build();
    }
}