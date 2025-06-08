package com.mangoboss.storage.subscription;

import lombok.Getter;

@Getter
public enum PlanType {
    BASIC("망고보스 BASIC 구독", 4900),
    PREMIUM("망고보스 PREMIUM 구독", 9900);

    private final String orderName;
    private final Integer amount;

    PlanType(String orderName, Integer amount) {
        this.orderName = orderName;
        this.amount = amount;
    }
}