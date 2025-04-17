package com.mangoboss.storage.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByKakaoId(final Long kakaoId);
}
