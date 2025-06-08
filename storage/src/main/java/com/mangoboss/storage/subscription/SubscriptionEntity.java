package com.mangoboss.storage.subscription;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "subscription")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @Column(nullable = false)
    private Long bossId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType planType;

    @Column(nullable = false)
    private LocalDate startedAt;

    @Column(nullable = false)
    private LocalDate expiredAt;

    @Column(nullable = false)
    private LocalDate nextPaymentDate;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private boolean autoRenewal;

    @Builder
    private SubscriptionEntity(Long bossId, PlanType planType, LocalDate startedAt,
                               LocalDate expiredAt, LocalDate nextPaymentDate,
                               boolean isActive, boolean autoRenewal) {
        this.bossId = bossId;
        this.planType = planType;
        this.startedAt = startedAt;
        this.expiredAt = expiredAt;
        this.nextPaymentDate = nextPaymentDate;
        this.isActive = isActive;
        this.autoRenewal = autoRenewal;
    }

    public static SubscriptionEntity create(Long bossId, PlanType planType) {
        final LocalDate now = LocalDate.now();
        return SubscriptionEntity.builder()
                .bossId(bossId)
                .planType(planType)
                .startedAt(now)
                .expiredAt(now)
                .nextPaymentDate(now)
                .isActive(true)
                .autoRenewal(true)
                .build();
    }

    public void extendNextMonth() {
        this.nextPaymentDate = this.nextPaymentDate.plusMonths(1);
        this.expiredAt = this.expiredAt.plusMonths(1);
    }

    public void reactivate(LocalDate newExpiredAt, LocalDate newNextPaymentDate) {
        this.isActive = true;
        this.expiredAt = newExpiredAt;
        this.nextPaymentDate = newNextPaymentDate;
    }

    public void updatePlan(PlanType planType) {
        this.planType = planType;
    }
}
