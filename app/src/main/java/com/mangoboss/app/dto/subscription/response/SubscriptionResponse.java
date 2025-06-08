package com.mangoboss.app.dto.subscription.response;

import com.mangoboss.storage.subscription.PlanType;
import com.mangoboss.storage.subscription.SubscriptionEntity;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SubscriptionResponse(
        PlanType planType,
        LocalDate startedAt,
        LocalDate expiredAt,
        LocalDate nextPaymentDate
) {
    public static SubscriptionResponse fromEntity(final SubscriptionEntity subscription) {
        return SubscriptionResponse.builder()
                .planType(subscription.getPlanType())
                .startedAt(subscription.getStartedAt())
                .expiredAt(subscription.getExpiredAt())
                .nextPaymentDate(subscription.getNextPaymentDate())
                .build();
    }
}