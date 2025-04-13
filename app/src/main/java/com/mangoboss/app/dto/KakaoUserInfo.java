package com.mangoboss.app.dto;

import java.time.LocalDate;

public record KakaoUserInfo(
	Long kakaoId,
	String email,
	String name,
	String picture,
	LocalDate birth,
	String phone
) {
	public static KakaoUserInfo create(final Long kakaoId, final String email, final String name, final String picture, final LocalDate birth, final String phone) {
		return new KakaoUserInfo(kakaoId, email, name, picture, birth, phone);
	}
}