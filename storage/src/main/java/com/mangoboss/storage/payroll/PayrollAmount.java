package com.mangoboss.storage.payroll;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayrollAmount {
    @Column(nullable = false)
    private Double totalTime;

    @Column(nullable = false)
    private Integer baseAmount;

    @Column(nullable = false)
    private Integer weeklyAllowance;

    @Column(nullable = false)
    private Integer totalAmount;

    @Column(nullable = false)
    private Integer withholdingTax;

    @Column(nullable = false)
    private Integer netAmount;

    @Builder
    private PayrollAmount(
            final Double totalTime,
            final Integer baseAmount,
            final Integer weeklyAllowance,
            final Integer totalAmount,
            final Integer withholdingTax,
            final Integer netAmount
    ) {
        this.totalTime = totalTime;
        this.baseAmount = baseAmount;
        this.weeklyAllowance = weeklyAllowance;
        this.totalAmount = totalAmount;
        this.withholdingTax = withholdingTax;
        this.netAmount = netAmount;
    }

    public static PayrollAmount create(
            final Double totalTime,
            final Integer baseAmount,
            final Integer weeklyAllowance,
            final Integer totalAmount,
            final Integer withholdingTax,
            final Integer netAmount
    ) {
        return PayrollAmount.builder()
                .totalTime(totalTime)
                .baseAmount(baseAmount)
                .weeklyAllowance(weeklyAllowance)
                .totalAmount(totalAmount)
                .withholdingTax(withholdingTax)
                .netAmount(netAmount)
                .build();
    }
}
