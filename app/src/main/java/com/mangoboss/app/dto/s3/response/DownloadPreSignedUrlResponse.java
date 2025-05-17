package com.mangoboss.app.dto.s3.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DownloadPreSignedUrlResponse(
        String url,
        LocalDateTime expiresAt
) {
    public static DownloadPreSignedUrlResponse of(String url, LocalDateTime expiresAt) {
        return DownloadPreSignedUrlResponse.builder()
                .url(url)
                .expiresAt(expiresAt)
                .build();
    }
}