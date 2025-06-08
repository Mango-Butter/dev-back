package com.mangoboss.storage.subscription;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "subscription_order")
@Getter
public class SubscriptionOrderEntity extends BaseTimeEntity {

    @Id
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(nullable = false)
    private Long bossId;

    @Column(nullable = false)
    private String billingKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType planType;

    @Column(nullable = false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    private String failReason;

    @Builder
    public SubscriptionOrderEntity(String orderId, Long bossId, String billingKey, PlanType planType, Integer amount, PaymentStatus paymentStatus, String failReason) {
        this.orderId = orderId;
        this.bossId = bossId;
        this.billingKey = billingKey;
        this.planType = planType;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.failReason = failReason;
    }

    public static SubscriptionOrderEntity success(String orderId, Long bossId, String billingKey, PlanType planType, Integer amount) {
        return SubscriptionOrderEntity.builder()
                .orderId(orderId)
                .bossId(bossId)
                .billingKey(billingKey)
                .planType(planType)
                .amount(amount)
                .paymentStatus(PaymentStatus.DONE)
                .failReason(null)
                .build();
    }

    public static SubscriptionOrderEntity fail(String orderId, Long bossId, String billingKey, PlanType planType, Integer amount, String failReason) {
        return SubscriptionOrderEntity.builder()
                .orderId(orderId)
                .bossId(bossId)
                .billingKey(billingKey)
                .planType(planType)
                .amount(amount)
                .paymentStatus(PaymentStatus.FAILED)
                .failReason(failReason)
                .build();
    }
}