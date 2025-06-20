package com.mangoboss.storage.payroll;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payslip")
public class PayslipEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_id")
    private PayrollEntity payroll;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayslipState payslipState;

    @Column(nullable = false)
    private Integer retryCount;

    private String fileKey;

    @Builder
    private PayslipEntity(final PayrollEntity payroll, final PayslipState payslipState, final Integer retryCount, final String fileKey) {
        this.payroll = payroll;
        this.payslipState = payslipState;
        this.fileKey = fileKey;
        this.retryCount = retryCount;
    }

    public static PayslipEntity create(final PayrollEntity payroll) {
        return PayslipEntity.builder()
                .payroll(payroll)
                .payslipState(PayslipState.PENDING)
                .retryCount(0)
                .fileKey(null)
                .build();
    }

    public void saveFileKey(final String fileKey) {
        this.fileKey = fileKey;
        this.payslipState = PayslipState.COMPLETED;
    }
}
