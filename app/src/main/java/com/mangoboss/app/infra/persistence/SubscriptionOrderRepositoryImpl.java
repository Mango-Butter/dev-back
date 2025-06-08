package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.SubscriptionOrderRepository;
import com.mangoboss.storage.subscription.SubscriptionOrderEntity;
import com.mangoboss.storage.subscription.SubscriptionOrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubscriptionOrderRepositoryImpl implements SubscriptionOrderRepository {

    private final SubscriptionOrderJpaRepository subscriptionOrderJpaRepository;

    @Override
    public void save(SubscriptionOrderEntity orderEntity) {
        subscriptionOrderJpaRepository.save(orderEntity);
    }

    @Override
    public List<SubscriptionOrderEntity> findByBossIdOrderByCreatedAtDesc(Long bossId) {
        return subscriptionOrderJpaRepository.findByBossIdOrderByCreatedAtDesc(bossId);
    }
}