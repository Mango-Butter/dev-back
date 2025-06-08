package com.mangoboss.app.infra.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.UserRepository;
import com.mangoboss.storage.user.UserEntity;
import com.mangoboss.storage.user.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public UserEntity getById(final Long userId) {
		return userJpaRepository.findById(userId)
			.orElseThrow(() -> new CustomException(CustomErrorInfo.USER_NOT_FOUND));
	}

	@Override
	public Optional<UserEntity> findByKakaoId(final Long kakaoId) {
		return userJpaRepository.findByKakaoId(kakaoId);
	}

	@Override
	public UserEntity save(final UserEntity userEntity) {
		userJpaRepository.save(userEntity);
		return userEntity;
	}
}
