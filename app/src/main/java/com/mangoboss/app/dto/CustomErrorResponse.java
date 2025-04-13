package com.mangoboss.app.dto;

import java.time.LocalDateTime;

public record CustomErrorResponse(
	int status,
	String message,
	String path,
	LocalDateTime timestamp
) {
}