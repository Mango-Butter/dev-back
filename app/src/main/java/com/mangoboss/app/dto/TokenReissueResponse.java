package com.mangoboss.app.dto;

public record TokenReissueResponse(String accessToken, String refreshToken) {
	public static TokenReissueResponse create(final String accessToken, final String refreshToken) {
		return new TokenReissueResponse(accessToken, refreshToken);
	}
}