package com.mangoboss.app.dto;

import com.mangoboss.storage.UserEntity;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserInfoResponse(
	@NotBlank
	String email,

	@NotBlank
	String name,

	@NotBlank
	String phone,

	@NotBlank
	String profileImageUrl,

	@NotBlank
	String role
) {
	public static UserInfoResponse fromEntity(UserEntity user) {
		return UserInfoResponse.builder()
			.email(user.getEmail())
			.name(user.getName())
			.phone(user.getPhone())
			.profileImageUrl(user.getProfileImageUrl())
			.role(user.getRole().toString())
			.build();
	}
}