package com.mangoboss.storage.user;

import com.mangoboss.storage.user.UserEntity;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByKakaoId(final Long kakaoId);
}
