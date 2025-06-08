package com.mangoboss.app.api.facade.subscription;

import com.mangoboss.app.domain.service.billing.BillingService;
import com.mangoboss.app.domain.service.subscription.SubscriptionService;
import com.mangoboss.app.dto.subscription.request.SubscriptionCreateRequest;
import com.mangoboss.app.dto.subscription.response.SubscriptionOrderResponse;
import com.mangoboss.app.dto.subscription.response.SubscriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionFacade {

    private final SubscriptionService subscriptionService;
    private final BillingService billingService;

    public void createOrReplaceSubscription(Long bossId, SubscriptionCreateRequest request) {
        billingService.validateBillingExists(bossId);
        subscriptionService.createOrReplaceSubscription(bossId, request.planType());
    }

    public SubscriptionResponse getSubscription(Long bossId) {
        final var subscription = subscriptionService.getSubscription(bossId);
        if (subscription == null) {
            return SubscriptionResponse.builder()
                    .planType(null)
                    .startedAt(null)
                    .expiredAt(null)
                    .nextPaymentDate(null)
                    .build();
        }
        return SubscriptionResponse.fromEntity(subscription);
    }

    public void cancelSubscriptionAndBilling(Long bossId) {
        subscriptionService.deleteSubscription(bossId);
        billingService.deleteBillingKey(bossId);
    }

    public List<SubscriptionOrderResponse> getOrderHistory(Long bossId) {
        return subscriptionService.getOrderHistory(bossId);
    }
}