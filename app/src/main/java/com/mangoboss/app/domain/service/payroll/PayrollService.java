package com.mangoboss.app.domain.service.payroll;


import com.mangoboss.app.domain.repository.EstimatedPayrollRepository;
import com.mangoboss.app.domain.repository.PayrollRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.payroll.PayrollAmount;
import com.mangoboss.storage.payroll.PayrollSettingEntity;
import com.mangoboss.storage.payroll.WithholdingType;
import com.mangoboss.storage.payroll.estimated.EstimatedPayrollEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayrollService {
    private static final int MINUTES_IN_HOUR = 60;
    private static final int WEEKLY_ALLOWANCE_THRESHOLD_HOURS = 15;
    private static final int WEEKLY_STANDARD_HOURS = 40;
    private static final int WEEKLY_ALLOWANCE_HOURS = 8;

    private final PayrollRepository payrollRepository;
    private final EstimatedPayrollRepository estimatedPayrollRepository;

    @Transactional
    public EstimatedPayrollEntity createEstimatedPayroll(final StaffEntity staff,
                                                         final PayrollSettingEntity setting,
                                                         final List<AttendanceEntity> attendances,
                                                         final LocalDate month) {
        PayrollAmount payrollAmount = createPayrollAmount(
                staff,
                setting.getDeductionUnit(),
                attendances,
                month
        );
        EstimatedPayrollEntity estimatedPayroll =  EstimatedPayrollEntity.create(
                payrollAmount,
                staff,
                month
        );
        return estimatedPayrollRepository.save(estimatedPayroll);
    }

    public PayrollAmount createPayrollAmount(final StaffEntity staff,
                                             final Integer deductionUnit,
                                             final List<AttendanceEntity> attendances,
                                             final LocalDate month) {
        Map<LocalDate, Integer> dailyWorkMinutesMap = groupTimeByWorkDate(deductionUnit, attendances);
        int weeklyAllowance = calculateWeeklyAllowance(dailyWorkMinutesMap, staff.getHourlyWage());
        int totalWorkMinutes = calculateTotalTimeMinutes(dailyWorkMinutesMap, month);
        int baseAmount = calculateBaseAmount(totalWorkMinutes, staff.getHourlyWage());
        int totalAmount = baseAmount + weeklyAllowance;
        int withholdingTax = calculateWithholdingTax(totalAmount, staff.getWithholdingType());
        int netAmount = totalAmount - withholdingTax;
        double totalTime = calculateTotalTime(totalWorkMinutes);

        return PayrollAmount.create(
                totalTime,
                baseAmount,
                weeklyAllowance,
                totalAmount,
                withholdingTax,
                netAmount
        );
    }

    private double calculateTotalTime(final Integer totalWorkMinutes) {
        double totalTime = (double) totalWorkMinutes / MINUTES_IN_HOUR;
        return Math.round(totalTime * 100) / 100.0;
    }

    private int calculateWithholdingTax(final Integer totalAmount, final WithholdingType withholdingType) {
        return (int) Math.floor(totalAmount * withholdingType.getRate());
    }

    private int calculateTotalTimeMinutes(final Map<LocalDate, Integer> dailyWorkMinutesMap, final LocalDate start) {
        return dailyWorkMinutesMap.entrySet().stream()
                .filter(entry -> !entry.getKey().isBefore(start))
                .mapToInt(Map.Entry::getValue)
                .sum();
    }

    private int calculateBaseAmount(final Integer totalWorkMinutes, final Integer hourlyWage) {
        return (int)(((double) totalWorkMinutes / 60) * hourlyWage);
    }

    private Map<LocalDate, Integer> groupTimeByWorkDate(final Integer deductionUnit,
                                                        final List<AttendanceEntity> attendances
    ) {
        return attendances.stream()
                .collect(Collectors.groupingBy(
                        att -> att.getSchedule().getWorkDate(),
                        Collectors.summingInt(att -> att.calculateWorkTime(deductionUnit))
                ));
    }

    private int calculateWeeklyAllowance(final Map<LocalDate, Integer> dailyWorkMinutesMap, final Integer hourlyWage) {
        int weeklyAllowanceThresholdMinutes = WEEKLY_ALLOWANCE_THRESHOLD_HOURS * MINUTES_IN_HOUR;
        int weeklyStandardMinutes = WEEKLY_STANDARD_HOURS * MINUTES_IN_HOUR;
        List<Integer> weeklyTotalTimes = dailyWorkMinutesMap.entrySet().stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)),
                        Collectors.summingInt(Map.Entry::getValue)
                ))
                .values().stream()
                .toList();
        return weeklyTotalTimes.stream()
                .mapToInt(weekly ->
                        weekly < weeklyAllowanceThresholdMinutes ? 0
                                : (int) ((double) weekly / weeklyStandardMinutes * WEEKLY_ALLOWANCE_HOURS * hourlyWage))
                .sum();
    }
}
