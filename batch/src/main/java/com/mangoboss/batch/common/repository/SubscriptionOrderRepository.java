package com.mangoboss.batch.common.repository;

import com.mangoboss.storage.subscription.SubscriptionOrderEntity;

public interface SubscriptionOrderRepository {
    void save(SubscriptionOrderEntity orderEntity);
}
