package com.mangoboss.app.dto.auth.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CustomErrorResponse(
	int status,
	String message,
	String path,
	LocalDateTime timestamp
) {
}