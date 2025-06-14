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

    @Builder
    private SubscriptionEntity(Long bossId, PlanType planType, LocalDate startedAt,
                               LocalDate expiredAt, LocalDate nextPaymentDate) {
        this.bossId = bossId;
        this.planType = planType;
        this.startedAt = startedAt;
        this.expiredAt = expiredAt;
        this.nextPaymentDate = nextPaymentDate;
    }

    public static SubscriptionEntity create(Long bossId, PlanType planType) {
        final LocalDate now = LocalDate.now();
        return SubscriptionEntity.builder()
                .bossId(bossId)
                .planType(planType)
                .startedAt(now)
                .expiredAt(now)
                .nextPaymentDate(now)
                .build();
    }

    public void extendNextMonth() {
        this.nextPaymentDate = this.nextPaymentDate.plusMonths(1);
        this.expiredAt = this.expiredAt.plusMonths(1);
    }
}
