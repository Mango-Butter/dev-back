package com.mangoboss.storage.payroll;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payslip")
public class PayslipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_id")
    private PayrollEntity payroll;

    @Column(nullable = false)
    private PayslipState payslipState;

    private String payslipPdfUrl;

    @Builder
    private PayslipEntity (final PayrollEntity payroll, final PayslipState payslipState){
        this.payroll = payroll;
        this.payslipState = payslipState;
        this.payslipPdfUrl = null;
    }

    public static PayslipEntity create(final PayrollEntity payroll){
        return PayslipEntity.builder()
                .payroll(payroll)
                .payslipState(PayslipState.PENDING)
                .build();
    }
}
