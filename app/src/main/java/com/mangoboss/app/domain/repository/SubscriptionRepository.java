package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.subscription.SubscriptionEntity;

public interface SubscriptionRepository {
    void save(SubscriptionEntity subscriptionEntity);
    SubscriptionEntity findByBossId(Long bossId);
    void delete(SubscriptionEntity subscriptionEntity);
    boolean existsByBossId(Long bossId);
}