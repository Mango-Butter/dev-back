package com.mangoboss.batch.billing;

import com.mangoboss.storage.user.UserEntity;

public interface UserRepository {
    UserEntity findById(Long id);
}
