package com.mangoboss.batch.billing;

import com.mangoboss.storage.user.UserEntity;
import com.mangoboss.storage.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserEntity findById(Long id) {
        return userJpaRepository.findById(id).orElseThrow();
    }
}
