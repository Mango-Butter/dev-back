package com.mangoboss.batch.billing;

import com.mangoboss.storage.subscription.SubscriptionOrderEntity;
import com.mangoboss.storage.subscription.SubscriptionOrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SubscriptionOrderRepositoryImpl implements SubscriptionOrderRepository {
    private final SubscriptionOrderJpaRepository subscriptionOrderJpaRepository;

    @Override
    public void save(SubscriptionOrderEntity orderEntity) {
        subscriptionOrderJpaRepository.save(orderEntity);
    }
}
