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
	public static KakaoUserInfo create(Long kakaoId, String email, String name, String picture, LocalDate birth, String phone) {
		return new KakaoUserInfo(kakaoId, email, name, picture, birth, phone);
	}
}