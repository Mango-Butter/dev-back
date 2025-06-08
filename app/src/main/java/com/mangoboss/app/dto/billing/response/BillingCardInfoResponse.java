package com.mangoboss.app.dto.billing.response;

import lombok.Builder;

@Builder
public record BillingCardInfoResponse(
        String cardCompany,   // 국민, 신한 등
        String cardNumber,    // 마스킹 카드번호
        String cardType,      // 신용, 체크 등
        String ownerType      // 개인, 법인
) {
}
