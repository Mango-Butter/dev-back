package com.mangoboss.batch.billing;

import com.mangoboss.storage.subscription.SubscriptionEntity;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository {
    void save(SubscriptionEntity subscription);
    List<SubscriptionEntity> findByNextPaymentDateAndIsActive(LocalDate nextPaymentDate, boolean isActive, Pageable pageable);
}