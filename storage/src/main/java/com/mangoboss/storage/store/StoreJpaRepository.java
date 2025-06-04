package com.mangoboss.storage.store;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreJpaRepository extends JpaRepository<StoreEntity, Long> {
    boolean existsByBusinessNumber(String businessNumber);

    boolean existsByInviteCode(String inviteCode);

    boolean existsByQrCode(String qrCode);

    Optional<StoreEntity> findByInviteCode(String inviteCode);

    List<StoreEntity> findAllByBossId(Long bossId);

    Optional<StoreEntity> findByIdAndBossId(Long id, Long bossId);
}