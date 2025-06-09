package com.mangoboss.batch.common.persistence;

import com.mangoboss.batch.common.repository.BillingRepository;
import com.mangoboss.storage.billing.BillingEntity;
import com.mangoboss.storage.billing.BillingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BillingRepositoryImpl implements BillingRepository {

    private final BillingJpaRepository billingJpaRepository;

    @Override
    public BillingEntity findByBossId(Long bossId) {
        return billingJpaRepository.findByBossId(bossId).orElseThrow();
    }
}
