package com.mangoboss.admin.infra.persistence;

import com.mangoboss.admin.common.exception.CustomErrorInfo;
import com.mangoboss.admin.common.exception.CustomException;
import com.mangoboss.admin.domain.repository.UserRepository;
import com.mangoboss.storage.user.Role;
import com.mangoboss.storage.user.UserEntity;
import com.mangoboss.storage.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

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

    @Override
    public Long countByRoleAndCreatedAtBetween(final Role role, final LocalDateTime start, final LocalDateTime end) {
        return userJpaRepository.countByRoleAndCreatedAtBetween(role, start, end);
    }

    @Override
    public Long countByCreatedAtBetween(final LocalDateTime start, final LocalDateTime end) {
        return userJpaRepository.countByCreatedAtBetween(start, end);
    }
}
