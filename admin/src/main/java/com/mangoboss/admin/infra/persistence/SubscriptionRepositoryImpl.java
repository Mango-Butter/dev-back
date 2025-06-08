package com.mangoboss.admin.infra.persistence;

import com.mangoboss.admin.domain.repository.SubscriptionRepository;
import com.mangoboss.storage.subscription.PlanTypeCountProjection;
import com.mangoboss.storage.subscription.SubscriptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    private final SubscriptionJpaRepository subscriptionJpaRepository;

    @Override
    public Long countActiveSubscriptions() {
        return subscriptionJpaRepository.countByIsActiveTrue();
    }

    @Override
    public List<PlanTypeCountProjection> countActiveSubscriptionsByPlanType() {
        return subscriptionJpaRepository.countActiveSubscriptionsByPlanType();
    }
}
