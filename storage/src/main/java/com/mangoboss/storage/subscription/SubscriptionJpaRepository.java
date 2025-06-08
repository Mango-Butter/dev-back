package com.mangoboss.storage.subscription;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByNextPaymentDate(LocalDate nextPaymentDate, Pageable pageable);

    Optional<SubscriptionEntity> findByBossId(Long bossId);

    boolean existsByBossId(Long bossId);

    @Query("""
                SELECT COUNT(s)
                FROM SubscriptionEntity s
            """)
    Long countTotalSubscriptions();

    @Query("""
                SELECT COUNT(s)
                FROM SubscriptionEntity s
                WHERE s.expiredAt >= :today
            """)
    Long countActiveSubscriptions(LocalDate today);

    @Query("""
                SELECT s.planType AS planType, COUNT(s) AS count
                FROM SubscriptionEntity s
                WHERE s.expiredAt >= :today
                GROUP BY s.planType
            """)
    List<PlanTypeCountProjection> countActiveSubscriptionsByPlanType(LocalDate today);
}
