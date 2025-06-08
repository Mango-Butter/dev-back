package com.mangoboss.app.domain.repository;

import java.util.Optional;

import com.mangoboss.storage.user.UserEntity;

public interface UserRepository {
	Optional<UserEntity> findByKakaoId(Long kakaoId);
	UserEntity save(UserEntity userEntity);
	UserEntity getById(Long id);
}