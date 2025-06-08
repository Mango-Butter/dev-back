package com.mangoboss.storage.billing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingJpaRepository extends JpaRepository<BillingEntity, Long> {
    Optional<BillingEntity> findByBossId(Long bossId);
}
