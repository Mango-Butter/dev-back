package com.mangoboss.app.api.facade.billing;

import com.mangoboss.app.domain.service.billing.BillingService;
import com.mangoboss.app.dto.billing.BillingRegisterRequest;
import com.mangoboss.app.dto.subscription.response.BillingCustomerKeyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BillingFacade {

    private final BillingService billingService;

    public void issueBillingKey(Long bossId, BillingRegisterRequest request) {
        billingService.issueBillingKey(bossId, request.customerKey(), request.authKey());
    }

    public BillingCustomerKeyResponse getOrCreateCustomerKey(Long bossId) {
        return billingService.getOrCreateCustomerKey(bossId);
    }
}