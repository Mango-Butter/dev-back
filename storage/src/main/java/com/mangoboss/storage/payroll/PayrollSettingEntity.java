package com.mangoboss.storage.payroll;

import com.mangoboss.storage.store.StoreEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payroll_setting")
public class PayrollSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_setting_id")
    private Long id;

    @Column(nullable = false)
    private Boolean autoTransferEnabled = false;

    @Min(1)
    @Max(28)
    private Integer transferDate;

    @Column(nullable = false)
    private Integer deductionUnit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_account_id")
    private TransferAccountEntity transferAccountEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @Builder
    private PayrollSettingEntity(
            final Boolean autoTransferEnabled,
            final Integer transferDate,
            final Integer deductionUnit,
            final TransferAccountEntity transferAccountEntity,
            final StoreEntity store
    ) {
        this.autoTransferEnabled = autoTransferEnabled;
        this.transferDate = transferDate;
        this.deductionUnit = deductionUnit;
        this.transferAccountEntity = transferAccountEntity;
        this.store = store;
    }

    public static PayrollSettingEntity init(
            final StoreEntity store
    ) {
        return PayrollSettingEntity.builder()
                .autoTransferEnabled(false)
                .transferDate(null)
                .deductionUnit(0)
                .transferAccountEntity(null)
                .store(store)
                .build();
    }

    public Boolean isAutoTransferEnabled() {
        return this.autoTransferEnabled;
    }

    public void registerTransferAccountEntity(final TransferAccountEntity transferAccountEntity) {
        this.transferAccountEntity = transferAccountEntity;
    }

    public boolean isTransferAccountNotSet() {
        return this.transferAccountEntity == null;
    }

    public PayrollSettingEntity updateAutoTransferEnabled(
            final Boolean autoTransferEnabled,
            final Integer transferDate
    ) {
        this.autoTransferEnabled = autoTransferEnabled;
        this.transferDate = transferDate;
        return this;
    }

    public PayrollSettingEntity updateDeductionUnit(final Integer deductionUnit) {
        this.deductionUnit = deductionUnit;
        return this;
    }
}
