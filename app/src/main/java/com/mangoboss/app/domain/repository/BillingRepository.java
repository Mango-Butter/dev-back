package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.billing.BillingEntity;

import java.util.Optional;

public interface BillingRepository {
    void save(BillingEntity billingEntity);
    Optional<BillingEntity> findByBossId(Long bossId);
}