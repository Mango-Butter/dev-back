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

    @Column(nullable = false)
    private PayslipState payslipState;

    @Column(nullable = false)
    private Integer retryCount;

    private String payslipPdfUrl;

    @Builder
    private PayslipEntity(final PayrollEntity payroll, final PayslipState payslipState, final Integer retryCount, final String payslipPdfUrl) {
        this.payroll = payroll;
        this.payslipState = payslipState;
        this.payslipPdfUrl = payslipPdfUrl;
        this.retryCount = retryCount;
    }

    public static PayslipEntity create(final PayrollEntity payroll) {
        return PayslipEntity.builder()
                .payroll(payroll)
                .payslipState(PayslipState.PENDING)
                .retryCount(0)
                .payslipPdfUrl(null)
                .build();
    }
}
