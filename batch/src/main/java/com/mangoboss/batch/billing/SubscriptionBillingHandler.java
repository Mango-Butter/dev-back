package com.mangoboss.batch.billing;

import com.mangoboss.storage.billing.BillingEntity;
import com.mangoboss.storage.subscription.SubscriptionEntity;
import com.mangoboss.storage.subscription.SubscriptionOrderEntity;
import com.mangoboss.storage.user.UserEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionBillingHandler {

    private final TossPaymentClient tossPaymentClient;
    private final SubscriptionOrderRepository orderRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BillingRepository billingRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Retryable(
            retryFor = {WebClientRequestException.class, TimeoutException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000)
    )
    @Transactional
    public void billingWithRetry(SubscriptionEntity subscription) {

        BillingEntity billing = billingRepository.findByBossId(subscription.getBossId());
        if (billing == null || billing.getBillingKey() == null || billing.getCustomerKey() == null) {
            log.warn("[Billing Skip] bossId={}, 이유: Billing 정보 없음", subscription.getBossId());
            return;
        }

        UserEntity boss = userRepository.findById(subscription.getBossId());
        String orderId = UUID.randomUUID().toString();

        try {
            tossPaymentClient.approveBilling(
                    billing.getBillingKey(),
                    billing.getCustomerKey(),
                    subscription.getPlanType(),
                    boss,
                    orderId
            );

            SubscriptionOrderEntity order = SubscriptionOrderEntity.success(
                    orderId, subscription.getBossId(), billing.getBillingKey(),
                    subscription.getPlanType(), subscription.getPlanType().getAmount());
            orderRepository.save(order);

            subscription.extendNextMonth();
            subscriptionRepository.save(subscription);
            entityManager.flush();

        } catch (Exception e) {
            SubscriptionOrderEntity order = SubscriptionOrderEntity.fail(
                    orderId, subscription.getBossId(), billing.getBillingKey(),
                    subscription.getPlanType(), subscription.getPlanType().getAmount(), e.getMessage());
            orderRepository.save(order);
            entityManager.flush();

            throw e; // 재시도 대상됨
        }
    }

    @Recover
    public void recover(Exception e, SubscriptionEntity subscription) {
        log.warn("[구독 Billing 최종 실패] subscriptionId={}", subscription.getId(), e);
    }
}
