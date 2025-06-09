package com.mangoboss.app.domain.service.subscription;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.SubscriptionOrderRepository;
import com.mangoboss.app.dto.subscription.response.SubscriptionOrderResponse;
import com.mangoboss.storage.subscription.PlanType;
import com.mangoboss.storage.subscription.SubscriptionEntity;
import com.mangoboss.app.domain.repository.SubscriptionRepository;
import com.mangoboss.storage.subscription.SubscriptionOrderEntity;
import com.mangoboss.storage.user.UserEntity;
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
    public void createOrReplaceSubscription(UserEntity boss, PlanType planType) {
        subscriptionRepository.findByBossId(boss.getId()).ifPresent(subscriptionRepository::delete);

        SubscriptionEntity subscription = SubscriptionEntity.create(boss.getId(), planType);
        subscriptionRepository.save(subscription);
        boss.addSubscription(subscription);
    }

    public SubscriptionEntity getSubscription(Long bossId) {
        return subscriptionRepository.findByBossId(bossId).orElse(null);
    }

    @Transactional
    public void deleteSubscription(Long bossId) {
        SubscriptionEntity subscription = subscriptionRepository.findByBossId(bossId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.SUBSCRIPTION_NOT_FOUND));
        subscriptionRepository.delete(subscription);
    }

    public List<SubscriptionOrderResponse> getOrderHistory(Long bossId) {
        List<SubscriptionOrderEntity> orders = subscriptionOrderRepository.findByBossIdOrderByCreatedAtDesc(bossId);
        return orders.stream()
                .map(SubscriptionOrderResponse::fromEntity)
                .toList();
    }

}