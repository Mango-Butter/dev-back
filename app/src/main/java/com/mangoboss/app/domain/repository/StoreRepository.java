package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.store.StoreEntity;

public interface StoreRepository {
    boolean existsByBusinessNumber(String businessNumber);

    boolean existsByInviteCode(String inviteCode);

    boolean existsByAttendanceQrCode(String qrCode);

    boolean existsByIdAndBossId(Long storeId, Long userId);

    StoreEntity save(StoreEntity storeEntity);

    StoreEntity getByInviteCode(String inviteCode);

    StoreEntity getById(Long id);
}
