package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.store.StoreEntity;

import java.util.List;

public interface StoreRepository {
    boolean existsByBusinessNumber(String businessNumber);

    boolean existsByInviteCode(String inviteCode);

    boolean existsByAttendanceQrCode(String qrCode);

    StoreEntity save(StoreEntity storeEntity);

    StoreEntity getByInviteCode(String inviteCode);

    StoreEntity getById(Long id);

    List<StoreEntity> findAllByBossId(Long bossId);

    StoreEntity getByIdAndBossId(Long id, Long bossId);
}
