package com.mangoboss.storage.store;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreJpaRepository extends JpaRepository<StoreEntity, Long> {
    boolean existsByBusinessNumber(String businessNumber);

    boolean existsByInviteCode(String inviteCode);

    boolean existsByQrCode(String qrCode);

    Optional<StoreEntity> findByInviteCode(String inviteCode);

    boolean existsByIdAndBossId(Long storeId, Long userId);
    List<StoreEntity> findAllByBossId(Long bossId);
}