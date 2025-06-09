package com.mangoboss.batch.common.repository;

import java.util.List;

public interface DeviceTokenRepository {
    List<String> findActiveTokensByUserId(Long userId);
}
