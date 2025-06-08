package com.mangoboss.app.domain.service.subscription;

import com.mangoboss.app.domain.repository.SubscriptionOrderRepository;
import com.mangoboss.app.dto.subscription.response.SubscriptionOrderResponse;
import com.mangoboss.storage.subscription.PlanType;
import com.mangoboss.storage.subscription.SubscriptionEntity;
import com.mangoboss.app.domain.repository.SubscriptionRepository;
import com.mangoboss.storage.subscription.SubscriptionOrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionOrderRepository subscriptionOrderRepository;

    @Transactional
    public void createOrReplaceSubscription(Long bossId, PlanType planType) {
        if (subscriptionRepository.existsByBossId(bossId)) {
            SubscriptionEntity existing = subscriptionRepository.findByBossId(bossId);
            subscriptionRepository.delete(existing);
        }
        SubscriptionEntity subscription = SubscriptionEntity.create(bossId, planType);
        subscriptionRepository.save(subscription);
    }

    public SubscriptionEntity getSubscription(Long bossId) {
        return subscriptionRepository.findByBossId(bossId);
    }

    @Transactional
    public void deleteSubscription(Long bossId) {
        SubscriptionEntity subscription = subscriptionRepository.findByBossId(bossId);
        subscriptionRepository.delete(subscription);
    }

    public List<SubscriptionOrderResponse> getOrderHistory(Long bossId) {
        List<SubscriptionOrderEntity> orders = subscriptionOrderRepository.findByBossIdOrderByCreatedAtDesc(bossId);
        return orders.stream()
                .map(SubscriptionOrderResponse::fromEntity)
                .toList();
    }

}