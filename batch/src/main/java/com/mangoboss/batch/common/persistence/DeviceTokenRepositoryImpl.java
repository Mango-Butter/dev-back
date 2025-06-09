package com.mangoboss.batch.common.persistence;

import com.mangoboss.batch.common.repository.DeviceTokenRepository;
import com.mangoboss.storage.notification.DeviceTokenEntity;
import com.mangoboss.storage.notification.DeviceTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DeviceTokenRepositoryImpl implements DeviceTokenRepository {
    private final DeviceTokenJpaRepository deviceTokenJpaRepository;

    @Override
    public List<String> findActiveTokensByUserId(final Long userId) {
        return deviceTokenJpaRepository.findByUserIdAndIsDeletedFalse(userId).stream()
                .map(DeviceTokenEntity::getTokenValue)
                .toList();
    }
}
