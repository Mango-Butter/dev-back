package com.mangoboss.admin.dto.dashboard;

import com.mangoboss.storage.subscription.PlanType;
import com.mangoboss.storage.subscription.PlanTypeCountProjection;
import lombok.Builder;

import java.util.List;

public record SubscriptionStatisticsResponse(
        Long totalCount,
        Long activeCount,
        Long inactiveCount,
        List<PlanTypeCount> planTypeCounts
) {
    public static record PlanTypeCount(
            PlanType planType,
            Long count
    ) {
        public static PlanTypeCount of(PlanTypeCountProjection projection) {
            return SubscriptionStatisticsResponse.PlanTypeCount.builder()
                    .planType(projection.getPlanType())
                    .count(projection.getCount())
                    .build();
        }

        @Builder
        public PlanTypeCount(PlanType planType, Long count) {
            this.planType = planType;
            this.count = count;
        }
    }

    public static List<PlanTypeCount> convert(List<PlanTypeCountProjection> projections) {
        return projections.stream()
                .map(PlanTypeCount::of)
                .toList();
    }

    public static SubscriptionStatisticsResponse of(Long totalCount, Long activeCount, Long inactiveCount, List<PlanTypeCount> planTypeCounts) {
        return SubscriptionStatisticsResponse.builder()
                .totalCount(totalCount)
                .activeCount(activeCount)
                .inactiveCount(inactiveCount)
                .planTypeCounts(planTypeCounts)
                .build();
    }

    @Builder
    public SubscriptionStatisticsResponse(Long totalCount, Long activeCount, Long inactiveCount, List<PlanTypeCount> planTypeCounts) {
        this.totalCount = totalCount;
        this.activeCount = activeCount;
        this.inactiveCount = inactiveCount;
        this.planTypeCounts = planTypeCounts;
    }
}