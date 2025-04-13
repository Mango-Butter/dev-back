package com.mangoboss.app.domain.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.UserRepository;
import com.mangoboss.app.dto.KakaoUserInfo;
import com.mangoboss.storage.Role;
import com.mangoboss.storage.UserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final UserRepository userRepository;

	public UserEntity findByKakaoIdOrThrow(Long kakaoId) {
		return userRepository.findByKakaoId(kakaoId)
			.orElseThrow(() -> new CustomException(CustomErrorInfo.USER_NOT_FOUND));
	}

	@Transactional
	public UserEntity getOrCreateUser(KakaoUserInfo kakaoUserInfo) {
		return userRepository.findByKakaoId(kakaoUserInfo.kakaoId())
			.orElseGet(() -> createUser(kakaoUserInfo));
	}

	@Transactional
	public UserEntity createUser(KakaoUserInfo info) {
		UserEntity userEntity = UserEntity.create(
			info.kakaoId(),
			info.name(),
			info.email(),
			info.phone(),
			info.birth(),
			info.picture(),
			Role.UNASSIGNED,
			LocalDateTime.now()
		);
		return userRepository.save(userEntity);
	}
}
