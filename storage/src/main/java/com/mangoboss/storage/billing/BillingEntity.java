package com.mangoboss.storage.billing;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "billing")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BillingEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "billing_id")
    private Long id;

    @Column(nullable = false)
    private Long bossId;

    @Column(nullable = false)
    private String customerKey;

    private String billingKey;

    @Column(columnDefinition = "json")
    private String cardData;

    @Builder
    private BillingEntity(Long bossId, String customerKey, String billingKey, String cardData) {
        this.bossId = bossId;
        this.customerKey = customerKey;
        this.billingKey = billingKey;
        this.cardData = cardData;
    }

    public static BillingEntity createPending(Long bossId, String customerKey) {
        return BillingEntity.builder()
                .bossId(bossId)
                .customerKey(customerKey)
                .billingKey(null)
                .cardData(null)
                .build();
    }

    public void registerBillingKey(String billingKey, String cardData) {
        this.billingKey = billingKey;
        this.cardData = cardData;
    }
}