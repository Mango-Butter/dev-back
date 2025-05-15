package com.mangoboss.storage.payroll;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payroll",
        uniqueConstraints = @UniqueConstraint(
        name = "uq_staff_month",
        columnNames = {"staff_id", "month"}
))
public class PayrollEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_id")
    private Long id;

    @Column(nullable = false, length = 7)
    @Length(min = 7, max = 7)
    private String month;

    @Column(nullable = false)
    private LocalDate transferDate;

    @Column(nullable = false)
    private LocalDate periodStart;

    @Column(nullable = false)
    private LocalDate periodEnd;

    @Column(nullable = false)
    private Integer totalAmount;

    @Column(columnDefinition = "jsonb")
    private String taxDeductions;

    @Column(nullable = false)
    private Integer netAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferState transferState;

    private LocalDateTime transferredAt;

    private String payslipUrl;




}
