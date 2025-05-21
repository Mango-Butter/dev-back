package com.mangoboss.storage.payroll.estimated;

import com.mangoboss.storage.payroll.PayrollAmount;
import com.mangoboss.storage.staff.StaffEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "estimated_payroll")
public class EstimatedPayrollEntity {
    @Id
    @Column(nullable = false)
    private String payrollKey; // payroll:staffId:yyyy-MM

    @Column(nullable = false)
    private Long staffId;

    @Column(nullable = false)
    private String bankCode;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private LocalDate month;

    @Column(nullable = false)
    private String withholdingType;

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
    private EstimatedPayrollEntity(String key,
                                    Long staffId,
                                    String bankCode,
                                    String account,
                                    LocalDate month,
                                    String withholdingType,
                                    Double totalTime,
                                    Integer baseAmount,
                                    Integer weeklyAllowance,
                                    Integer totalAmount,
                                    Integer withholdingTax,
                                    Integer netAmount) {
        this.payrollKey = key;
        this.staffId = staffId;
        this.bankCode = bankCode;
        this.account = account;
        this.month = month;
        this.withholdingType = withholdingType;
        this.totalTime = totalTime;
        this.baseAmount = baseAmount;
        this.weeklyAllowance = weeklyAllowance;
        this.totalAmount = totalAmount;
        this.withholdingTax = withholdingTax;
        this.netAmount = netAmount;
    }

    public static EstimatedPayrollEntity create(final PayrollAmount payrollAmount,
                                                final StaffEntity staff,
                                                final LocalDate month
    ) {
        return EstimatedPayrollEntity.builder()
                .key(generateKey(staff.getId(), month))
                .staffId(staff.getId())
                .bankCode(staff.getBankCode().getDisplayName())
                .account(staff.getAccount())
                .month(month)
                .withholdingType(staff.getWithholdingType().getLabel())
                .totalTime(payrollAmount.getTotalTime())
                .baseAmount(payrollAmount.getBaseAmount())
                .weeklyAllowance(payrollAmount.getWeeklyAllowance())
                .totalAmount(payrollAmount.getTotalAmount())
                .withholdingTax(payrollAmount.getWithholdingTax())
                .netAmount(payrollAmount.getNetAmount())
                .build();
    }

    private static String generateKey(final Long staffId, final LocalDate month){
        String formattedMonth = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return String.format("payroll:%d:%s", staffId, formattedMonth);
    }
}