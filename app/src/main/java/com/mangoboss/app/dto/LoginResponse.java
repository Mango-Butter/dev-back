package com.mangoboss.app.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
	String accessToken,
	String refreshToken
) {
}