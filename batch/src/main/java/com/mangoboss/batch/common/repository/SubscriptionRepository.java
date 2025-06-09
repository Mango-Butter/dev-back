package com.mangoboss.batch.common.repository;

import com.mangoboss.storage.subscription.SubscriptionEntity;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository {
    void save(SubscriptionEntity subscription);
    List<SubscriptionEntity> findByNextPaymentDate(LocalDate nextPaymentDate, Pageable pageable);
}