package com.mangoboss.app.infra.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.mangoboss.app.domain.repository.UserRepository;
import com.mangoboss.storage.UserEntity;
import com.mangoboss.storage.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<UserEntity> findByKakaoId(final Long kakaoId) {
		return userJpaRepository.findByKakaoId(kakaoId);
	}

	@Override
	public Optional<UserEntity> findByUserId(Long userId) {
		return userJpaRepository.findById(userId);
	}

	@Override
	public UserEntity save(final UserEntity userEntity) {
		userJpaRepository.save(userEntity);
		return userEntity;
	}
}
