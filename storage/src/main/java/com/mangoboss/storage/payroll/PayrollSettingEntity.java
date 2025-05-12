package com.mangoboss.storage.payroll;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payroll_setting")
public class PayrollSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean autoTransferEnabled = false;

    private Integer transfer_date;

    @Column(nullable = false)
    private Integer overtimeLimit;

    @Column(nullable = false)
    private Integer deduction_unit = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_account_id", nullable = false)
    private TransferAccountEntity transferAccountEntity;
}
