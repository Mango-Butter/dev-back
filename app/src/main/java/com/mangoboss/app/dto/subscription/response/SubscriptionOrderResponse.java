package com.mangoboss.app.dto.subscription.response;

import com.mangoboss.storage.subscription.PaymentStatus;
import com.mangoboss.storage.subscription.PlanType;
import com.mangoboss.storage.subscription.SubscriptionOrderEntity;

import java.time.LocalDateTime;

public record SubscriptionOrderResponse(
        String orderId,
        PlanType planType,
        Integer amount,
        PaymentStatus paymentStatus,
        String failReason,
        LocalDateTime createdAt
) {
    public static SubscriptionOrderResponse fromEntity(SubscriptionOrderEntity order) {
        return new SubscriptionOrderResponse(
                order.getOrderId(),
                order.getPlanType(),
                order.getAmount(),
                order.getPaymentStatus(),
                order.getFailReason(),
                order.getCreatedAt()
        );
    }
}