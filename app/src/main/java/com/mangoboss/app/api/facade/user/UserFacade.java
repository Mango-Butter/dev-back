package com.mangoboss.app.api.facade.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.app.dto.UserInfoResponse;
import com.mangoboss.storage.user.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFacade {
	private final UserService userService;

	@Transactional
	public UserInfoResponse getUserInfo(final CustomUserDetails userDetails) {
		UserEntity user = userService.getByUserId(userDetails.getUserId());
		return UserInfoResponse.fromEntity(user);
	}
}
