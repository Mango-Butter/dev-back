package com.mangoboss.app.dto.user;

import com.mangoboss.storage.user.Role;
import com.mangoboss.storage.user.UserEntity;
import java.time.LocalDate;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import lombok.Builder;

@Builder
public record KakaoUserInfo(
	Long kakaoId,
	String email,
	String name,
	String profileImageUrl,
	LocalDate birth,
	String phone
) {
	public static KakaoUserInfo create(final Long kakaoId, final String email, final String name, final String profileImageUrl, final LocalDate birth, final String phone) {
		return KakaoUserInfo.builder()
				.kakaoId(kakaoId)
				.email(email)
				.name(name)
				.profileImageUrl(profileImageUrl)
				.birth(birth)
				.phone(phone)
				.build();
	}

	public UserEntity toEntity(final Role role) {
		return UserEntity.create(
			kakaoId,
			name,
			email,
			phone,
			birth,
			profileImageUrl,
			role
		);
	}

	public void validate() {
		if (email == null || name == null || profileImageUrl == null || birth == null || phone == null)  {
			throw new CustomException(CustomErrorInfo.KAKAO_USER_INFO_INCOMPLETE);
		}
	}
}