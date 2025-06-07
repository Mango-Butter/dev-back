package com.mangoboss.admin.infra.persistence;

import com.mangoboss.admin.domain.repository.StoreRepository;
import com.mangoboss.storage.store.StoreJpaRepository;
import com.mangoboss.storage.store.StoreType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {

    private final StoreJpaRepository storeJpaRepository;

    @Override
    public Long countByCreatedAtBetween(final LocalDateTime start, final LocalDateTime end) {
        return storeJpaRepository.countByCreatedAtBetween(start, end);
    }

    @Override
    public Long countByStoreTypeAndCreatedAtBetween(final StoreType storeType, final LocalDateTime start, final LocalDateTime end) {
        return storeJpaRepository.countByStoreTypeAndCreatedAtBetween(storeType, start, end);
    }

    @Override
    public Long countByBossId(final Long userId) {
        return storeJpaRepository.countByBossId(userId);
    }
}