package com.mangoboss.app.dto.user.requeset;

import lombok.Builder;

@Builder
public record RefreshTokenRequest(
	String refreshToken
) {
}