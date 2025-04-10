package com.mangoboss.app.dto;

import com.mangoboss.storage.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class UserInfoDto {
		private Long userId;
		private Long kakaoId;
		private String name;
		private String email;
		private String phone;
		private Role role;
	}
}
