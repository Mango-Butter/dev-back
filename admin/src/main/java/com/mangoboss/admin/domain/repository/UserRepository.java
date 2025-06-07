package com.mangoboss.admin.domain.repository;

import com.mangoboss.storage.user.Role;
import com.mangoboss.storage.user.UserEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByKakaoId(Long kakaoId);

    UserEntity save(UserEntity userEntity);

    UserEntity getById(Long id);

    Long countByRoleAndCreatedAtBetween(Role role, LocalDateTime start, LocalDateTime end);

    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}