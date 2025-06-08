package com.mangoboss.batch.billing;

import com.mangoboss.storage.subscription.SubscriptionOrderEntity;

public interface SubscriptionOrderRepository {
    void save(SubscriptionOrderEntity orderEntity);
}
