package com.mangoboss.batch.billing;

import com.mangoboss.storage.subscription.SubscriptionEntity;
import com.mangoboss.storage.subscription.SubscriptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SubscriptionJpaRepository subscriptionJpaRepository;

    @Override
    public void save(SubscriptionEntity subscription) {
        subscriptionJpaRepository.save(subscription);
    }

    @Override
    public List<SubscriptionEntity> findByNextPaymentDate(LocalDate nextPaymentDate, Pageable pageable) {
        return subscriptionJpaRepository.findByNextPaymentDate(nextPaymentDate, pageable);
    }
}
