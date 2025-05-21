//package com.mangoboss.app.domain.service.payroll;
//
//import com.mangoboss.storage.payroll.PayrollAmount;
//import com.mangoboss.storage.staff.StaffEntity;
//import lombok.*;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Builder
//public class EstimatedPayroll {
//    private String key;
//    private Long staffId;
//    private String bankCode;
//    private String account;
//    private LocalDate month;
//    private String withholdingType;
//    private Double totalTime;
//    private Integer baseAmount;
//    private Integer weeklyAllowance;
//    private Integer totalAmount;
//    private Integer withholdingTax;
//    private Integer netAmount;
//
//    public static EstimatedPayroll create(final PayrollAmount payrollAmount,
//                                          final StaffEntity staff,
//                                          final LocalDate month
//    ) {
//        return EstimatedPayroll.builder()
//                .key(generateKey(staff.getId(), month))
//                .staffId(staff.getId())
//                .bankCode(staff.getBankCode().getDisplayName())
//                .account(staff.getAccount())
//                .month(month)
//                .withholdingType(staff.getWithholdingType().getLabel())
//                .totalTime(payrollAmount.getTotalTime())
//                .baseAmount(payrollAmount.getBaseAmount())
//                .weeklyAllowance(payrollAmount.getWeeklyAllowance())
//                .totalAmount(payrollAmount.getTotalAmount())
//                .withholdingTax(payrollAmount.getWithholdingTax())
//                .netAmount(payrollAmount.getNetAmount())
//                .build();
//    }
//
//    private static String generateKey(final Long staffId, final LocalDate month){
//        String formattedMonth = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
//        return String.format("payroll:%d:%s", staffId, formattedMonth);
//    }
//}