package com.mangoboss.storage.subscription;

import lombok.Getter;

@Getter
public enum PlanType {
    PREMIUM("망고보스 PREMIUM 구독", 19900);

    private final String orderName;
    private final Integer amount;

    PlanType(String orderName, Integer amount) {
        this.orderName = orderName;
        this.amount = amount;
    }
}