package com.mangoboss.app.domain.repository;

import java.util.Optional;

import com.mangoboss.storage.UserEntity;

public interface UserRepository {
	Optional<UserEntity> findByKakaoId(final Long kakaoId);
	Optional<UserEntity> findByUserId(final Long userId);
	UserEntity save(final UserEntity userEntity);
}