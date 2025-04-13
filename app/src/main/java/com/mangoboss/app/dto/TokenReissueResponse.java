package com.mangoboss.app.dto;

public record TokenReissueResponse(String accessToken, String refreshToken) {
	public static TokenReissueResponse create(String accessToken, String refreshToken) {
		return new TokenReissueResponse(accessToken, refreshToken);
	}
}