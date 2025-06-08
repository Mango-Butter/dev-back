package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.BillingRepository;
import com.mangoboss.storage.billing.BillingEntity;
import com.mangoboss.storage.billing.BillingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BillingRepositoryImpl implements BillingRepository {

    private final BillingJpaRepository billingJpaRepository;

    @Override
    public void save(BillingEntity billingEntity) {
        billingJpaRepository.save(billingEntity);
    }

    @Override
    public Optional<BillingEntity> findByBossId(Long bossId) {
        return billingJpaRepository.findByBossId(bossId);
    }
}
