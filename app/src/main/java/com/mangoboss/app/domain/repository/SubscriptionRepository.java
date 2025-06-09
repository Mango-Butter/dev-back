package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.subscription.SubscriptionEntity;

import java.util.Optional;

public interface SubscriptionRepository {
    SubscriptionEntity save(SubscriptionEntity subscriptionEntity);
    Optional<SubscriptionEntity> findByBossId(Long bossId);
    void delete(SubscriptionEntity subscriptionEntity);
    boolean existsByBossId(Long bossId);
}