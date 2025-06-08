package com.mangoboss.app.dto.subscription.request;

import com.mangoboss.storage.subscription.PlanType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SubscriptionCreateRequest(
        @NotNull PlanType planType
) {}