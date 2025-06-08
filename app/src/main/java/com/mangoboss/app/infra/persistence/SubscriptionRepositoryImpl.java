package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.SubscriptionRepository;
import com.mangoboss.storage.subscription.SubscriptionEntity;
import com.mangoboss.storage.subscription.SubscriptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    private final SubscriptionJpaRepository subscriptionJpaRepository;

    @Override
    public void save(SubscriptionEntity subscriptionEntity) {
        subscriptionJpaRepository.save(subscriptionEntity);
    }

    @Override
    public Optional<SubscriptionEntity> findByBossId(Long bossId) {
        return subscriptionJpaRepository.findByBossId(bossId);
    }

    @Override
    public void delete(SubscriptionEntity subscriptionEntity) {
        subscriptionJpaRepository.delete(subscriptionEntity);
    }

    @Override
    public boolean existsByBossId(Long bossId) {
        return subscriptionJpaRepository.existsByBossId(bossId);
    }
}
