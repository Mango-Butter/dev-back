package com.mangoboss.batch.common.repository;

import com.mangoboss.storage.billing.BillingEntity;

public interface BillingRepository {
    BillingEntity findByBossId(Long bossId);
}
