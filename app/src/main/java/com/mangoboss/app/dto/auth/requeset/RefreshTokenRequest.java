package com.mangoboss.app.dto.auth.requeset;

import lombok.Builder;

@Builder
public record RefreshTokenRequest(
	String refreshToken
) {
}