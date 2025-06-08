package com.mangoboss.storage.subscription;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByNextPaymentDateAndIsActive(LocalDate nextPaymentDate, boolean isActive, Pageable pageable);

    Optional<SubscriptionEntity> findByBossId(Long bossId);

    boolean existsByBossId(Long bossId);

    Long countByIsActiveTrue();

    @Query("""
                SELECT s.planType AS planType, COUNT(s) AS count
                FROM SubscriptionEntity s
                WHERE s.isActive = true
                GROUP BY s.planType
            """)
    List<PlanTypeCountProjection> countActiveSubscriptionsByPlanType();
}
