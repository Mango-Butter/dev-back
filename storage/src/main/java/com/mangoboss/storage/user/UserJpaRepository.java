package com.mangoboss.storage.user;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByKakaoId(final Long kakaoId);

	Long countByRoleAndCreatedAtBetween(Role role, LocalDateTime start, LocalDateTime end);

	Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
