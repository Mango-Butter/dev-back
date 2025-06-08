package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.subscription.SubscriptionOrderEntity;

import java.util.List;

public interface SubscriptionOrderRepository {
    void save(SubscriptionOrderEntity orderEntity);
    List<SubscriptionOrderEntity> findByBossIdOrderByCreatedAtDesc(Long bossId);
}