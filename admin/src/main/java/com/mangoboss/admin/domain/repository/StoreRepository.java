package com.mangoboss.admin.domain.repository;

import com.mangoboss.storage.store.StoreType;

import java.time.LocalDateTime;

public interface StoreRepository {
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    Long countByStoreTypeAndCreatedAtBetween(StoreType storeType, LocalDateTime start, LocalDateTime end);

    Long countByBossId(Long bossId);
}