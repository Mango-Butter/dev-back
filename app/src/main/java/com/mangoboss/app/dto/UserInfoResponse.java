package com.mangoboss.app.dto;

import com.mangoboss.storage.UserEntity;

import lombok.Builder;

@Builder
public record UserInfoResponse(
	Long userId,
	String email,
	String name,
	String phone,
	String profileImageUrl,
	String role
) {
	public static UserInfoResponse fromEntity(final UserEntity user) {
		return UserInfoResponse.builder()
			.userId(user.getUserId())
			.email(user.getEmail())
			.name(user.getName())
			.phone(user.getPhone())
			.profileImageUrl(user.getProfileImageUrl())
			.role(user.getRole().toString())
			.build();
	}
}