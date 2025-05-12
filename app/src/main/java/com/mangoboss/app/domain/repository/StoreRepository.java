package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.store.StoreEntity;

import java.util.List;

public interface StoreRepository {
    boolean existsByBusinessNumber(String businessNumber);

    boolean existsByInviteCode(String inviteCode);

    boolean existsByAttendanceQrCode(String qrCode);

    boolean existsByIdAndBossId(Long id, Long userId);

    StoreEntity save(StoreEntity storeEntity);

    StoreEntity getByInviteCode(String inviteCode);

    StoreEntity getById(Long id);

    List<StoreEntity> findAllByBossId(Long bossId);

    List<StoreEntity> findAllByUserId(Long userId);

}
