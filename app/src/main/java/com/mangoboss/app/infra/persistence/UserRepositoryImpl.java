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
	public Optional<UserEntity> findByKakaoId(Long kakaoId) {
		return userJpaRepository.findByKakaoId(kakaoId);
	}

	@Override
	public UserEntity save(UserEntity userEntity) {
		userJpaRepository.save(userEntity);
		return userEntity;
	}
}
