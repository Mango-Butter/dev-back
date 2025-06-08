package com.mangoboss.storage.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionOrderJpaRepository extends JpaRepository<SubscriptionOrderEntity, String> {
    List<SubscriptionOrderEntity> findByBossIdOrderByCreatedAtDesc(Long bossId);
}
