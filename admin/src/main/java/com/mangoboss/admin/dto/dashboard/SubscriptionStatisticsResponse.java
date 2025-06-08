package com.mangoboss.admin.dto.dashboard;

import com.mangoboss.storage.subscription.PlanType;

import java.util.List;

public record SubscriptionStatisticsResponse(
        Long activeCount,
        List<PlanTypeCount> planTypeCounts
) {
    public static record PlanTypeCount(
            PlanType planType,
            Long count
    ) {}
}