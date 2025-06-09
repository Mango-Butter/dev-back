package com.mangoboss.batch.common.repository;

import com.mangoboss.storage.user.UserEntity;

public interface UserRepository {
    UserEntity findById(Long id);
}
