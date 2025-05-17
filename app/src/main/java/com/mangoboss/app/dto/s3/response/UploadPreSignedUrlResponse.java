package com.mangoboss.app.dto.s3.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UploadPreSignedUrlResponse(
        String uploadUrl,
        String fileKey,
        LocalDateTime expiresAt
) {
    public static UploadPreSignedUrlResponse of(
            final String uploadUrl,
            final LocalDateTime expiresAt,
            final String fileKey
    ) {
        return UploadPreSignedUrlResponse.builder()
                .uploadUrl(uploadUrl)
                .expiresAt(expiresAt)
                .fileKey(fileKey)
                .build();
    }
}