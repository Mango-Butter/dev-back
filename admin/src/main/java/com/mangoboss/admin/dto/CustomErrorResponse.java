package com.mangoboss.admin.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CustomErrorResponse(
        int status,
        String message,
        String path,
        LocalDateTime timestamp
) {
}