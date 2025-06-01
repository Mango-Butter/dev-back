package com.mangoboss.app.domain.repository;

import java.util.List;

public interface DeviceTokenRepository {
    List<String> findActiveTokensByUserId(Long userId);

    boolean existsByUserIdAndTokenValue(Long userId, String tokenValue);

    void save(Long userId, String tokenValue);

    void updateIsDeletedFalseAndModifiedAt(Long userId, String tokenValue);
}
