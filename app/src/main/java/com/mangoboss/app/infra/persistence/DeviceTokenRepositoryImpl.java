package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.DeviceTokenRepository;
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

    @Override
    public boolean existsByUserIdAndTokenValue(final Long userId, final String tokenValue) {
        return deviceTokenJpaRepository.existsByUserIdAndTokenValue(userId, tokenValue);
    }

    @Override
    public void save(final Long userId, final String tokenValue) {
        final DeviceTokenEntity deviceToken = DeviceTokenEntity.create(userId, tokenValue);
        deviceTokenJpaRepository.save(deviceToken);
    }

    @Override
    public void updateIsDeletedFalseAndModifiedAt(final Long userId, final String tokenValue) {
        deviceTokenJpaRepository.updateIsDeletedFalseAndModifiedAt(userId, tokenValue);
    }
}