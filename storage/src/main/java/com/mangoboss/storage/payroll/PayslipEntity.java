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

    private String payslipPdfKey;

    @Builder
    private PayslipEntity(final PayrollEntity payroll, final PayslipState payslipState, final Integer retryCount, final String payslipPdfKey) {
        this.payroll = payroll;
        this.payslipState = payslipState;
        this.payslipPdfKey = payslipPdfKey;
        this.retryCount = retryCount;
    }

    public static PayslipEntity create(final PayrollEntity payroll) {
        return PayslipEntity.builder()
                .payroll(payroll)
                .payslipState(PayslipState.PENDING)
                .retryCount(0)
                .payslipPdfKey(null)
                .build();
    }

    public void savePayslipPdfKey(final String payslipPdfKey) {
        this.payslipPdfKey = payslipPdfKey;
        this.payslipState = PayslipState.COMPLETED;
    }
}
