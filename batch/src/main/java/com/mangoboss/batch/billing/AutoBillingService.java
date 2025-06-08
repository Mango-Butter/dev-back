package com.mangoboss.batch.billing;

import com.mangoboss.storage.subscription.SubscriptionEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoBillingService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionBillingHandler billingHandler;
    private final Clock clock;
    private static final int BATCH_SIZE = 10;

    @Transactional
    public void autoBilling() {
        LocalDate today = LocalDate.now(clock);

        List<SubscriptionEntity> subscriptions =
                subscriptionRepository.findByNextPaymentDateAndIsActive(today, true, PageRequest.of(0, BATCH_SIZE));

        subscriptions.forEach(subscription -> {
            try {
                billingHandler.billingWithRetry(subscription);
            } catch (Exception e) {
                log.warn("[자동결제 실패] subscriptionId={}, error={}", subscription.getId(), e.getMessage());
            }
        });
    }
}