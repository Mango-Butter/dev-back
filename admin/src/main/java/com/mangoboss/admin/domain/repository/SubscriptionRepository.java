package com.mangoboss.admin.domain.repository;

import com.mangoboss.storage.subscription.PlanTypeCountProjection;

import java.util.List;

public interface SubscriptionRepository {
    Long countTotalSubscriptions();
    Long countActiveSubscriptions();
    List<PlanTypeCountProjection> countActiveSubscriptionsByPlanType();
}
