package com.mangoboss.app.dto.subscription.response;

import lombok.Builder;

@Builder
public record BillingCustomerKeyResponse(
        String customerKey
) {
    public static BillingCustomerKeyResponse of(String customerKey) {
        return BillingCustomerKeyResponse.builder()
                .customerKey(customerKey)
                .build();
    }
}