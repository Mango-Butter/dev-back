package com.mangoboss.app.api.facade.subscription;

import com.mangoboss.app.domain.service.subscription.SubscriptionService;
import com.mangoboss.app.dto.subscription.request.SubscriptionCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionFacade {

    private final SubscriptionService subscriptionService;

    public void createOrReplaceSubscription(Long bossId, SubscriptionCreateRequest request) {
        subscriptionService.createOrReplaceSubscription(bossId, request.planType());
    }
}