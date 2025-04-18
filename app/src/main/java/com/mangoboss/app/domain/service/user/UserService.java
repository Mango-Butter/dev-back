package com.mangoboss.app.domain.service.user;

import com.mangoboss.app.common.security.KakaoSocialLogin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.UserRepository;
import com.mangoboss.app.dto.KakaoUserInfo;
import com.mangoboss.storage.user.Role;
import com.mangoboss.storage.user.UserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
	private final UserRepository userRepository;
	private final KakaoSocialLogin kakaoSocialLogin;

	@Transactional(readOnly = true)
	public UserEntity getByUserId(final Long userId) {
		return userRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(CustomErrorInfo.USER_NOT_FOUND));
	}

	public UserEntity getOrCreateUser(final KakaoUserInfo kakaoUserInfo) {
		return userRepository.findByKakaoId(kakaoUserInfo.kakaoId())
			.orElseGet(() -> createUserByKakao(kakaoUserInfo));
	}

	public UserEntity createUserByKakao(final KakaoUserInfo kakaoUserInfo) {
		final UserEntity userEntity = kakaoUserInfo.toEntity(Role.UNASSIGNED);
		return userRepository.save(userEntity);
	}
}
