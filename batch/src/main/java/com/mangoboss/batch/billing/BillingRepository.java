package com.mangoboss.batch.billing;

import com.mangoboss.storage.billing.BillingEntity;

public interface BillingRepository {
    BillingEntity findByBossId(Long bossId);
}
