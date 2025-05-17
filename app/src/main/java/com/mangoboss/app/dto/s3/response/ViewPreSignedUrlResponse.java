package com.mangoboss.app.dto.s3.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ViewPreSignedUrlResponse(
        String url,
        LocalDateTime expiresAt
) {
    public static ViewPreSignedUrlResponse of(final String url, final LocalDateTime expiresAt) {
        return ViewPreSignedUrlResponse.builder()
                .url(url)
                .expiresAt(expiresAt)
                .build();
    }
}
