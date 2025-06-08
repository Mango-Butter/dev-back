package com.mangoboss.app.domain.service.subscription;

import com.mangoboss.storage.subscription.PlanType;
import com.mangoboss.storage.subscription.SubscriptionEntity;
import com.mangoboss.app.domain.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public void createOrReplaceSubscription(Long bossId, PlanType planType) {
        if (subscriptionRepository.existsByBossId(bossId)) {
            SubscriptionEntity existing = subscriptionRepository.findByBossId(bossId);
            subscriptionRepository.delete(existing);
        }
        SubscriptionEntity subscription = SubscriptionEntity.create(bossId, planType);
        subscriptionRepository.save(subscription);
    }
}