package com.mangoboss.app.domain.service.payroll;


import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.repository.EstimatedPayrollRepository;
import com.mangoboss.app.domain.repository.PayrollRepository;
import com.mangoboss.app.domain.repository.PayslipRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.payroll.*;
import com.mangoboss.storage.payroll.estimated.EstimatedPayrollEntity;
import com.mangoboss.storage.payroll.projection.PayrollWithPayslipProjection;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
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
    private final S3FileManager s3FileManager;
    private final Clock clock;

    @Transactional
    public EstimatedPayrollEntity createEstimatedPayroll(final StaffEntity staff,
                                                         final PayrollSettingEntity setting,
                                                         final List<AttendanceEntity> attendances,
                                                         final LocalDate targetMonth) {
        PayrollAmount payrollAmount = createPayrollAmount(
                staff,
                setting,
                attendances,
                targetMonth
        );
        EstimatedPayrollEntity estimatedPayroll = EstimatedPayrollEntity.create(
                payrollAmount,
                staff,
                targetMonth
        );
        return estimatedPayrollRepository.save(estimatedPayroll);
    }

    public PayrollAmount createPayrollAmount(final StaffEntity staff,
                                             final PayrollSettingEntity payrollSetting,
                                             final List<AttendanceEntity> attendances,
                                             final LocalDate targetMonth) {
        Map<LocalDate, Integer> dailyWorkMinutesMap = groupTimeByWorkDate(payrollSetting.getDeductionUnit(), attendances);
        int weeklyAllowance = calculateWeeklyAllowance(dailyWorkMinutesMap, staff.getHourlyWage());
        int totalCommutingAllowance = calculateCommutingAllowance(dailyWorkMinutesMap, payrollSetting.getCommutingAllowance());
        int totalWorkMinutes = calculateTotalTimeMinutes(dailyWorkMinutesMap, targetMonth);
        int baseAmount = calculateBaseAmount(totalWorkMinutes, staff.getHourlyWage());
        int totalAmount = baseAmount + weeklyAllowance;
        int withholdingTax = calculateWithholdingTax(totalAmount, staff.getWithholdingType());
        int netAmount = totalAmount - withholdingTax;
        double totalTime = calculateTotalTime(totalWorkMinutes);

        return PayrollAmount.create(
                totalTime,
                baseAmount,
                weeklyAllowance,
                totalCommutingAllowance,
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
        return (int) (((double) totalWorkMinutes / 60) * hourlyWage);
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

    private int calculateCommutingAllowance(final Map<LocalDate, Integer> dailyWorkMinutesMap, final Integer commutingAllowance) {
        return commutingAllowance * dailyWorkMinutesMap.size();
    }

    @Transactional
    public List<PayrollEntity> confirmEstimatedPayroll(final StoreEntity store, final PayrollSettingEntity payrollSetting,
                                                       final List<String> keys) {
        List<EstimatedPayrollEntity> estimatedPayrolls = estimatedPayrollRepository.findAllByPayrollKeyIn(keys);
        List<PayrollEntity> payrolls = estimatedPayrolls.stream()
                .map(estimated -> estimated.createPayrollEntity(store, payrollSetting))
                .toList();
        return payrollRepository.saveAll(payrolls);
    }

    @Transactional
    public void deletePayrollsByStoreIdAndMonth(final Long storeId, final LocalDate targetMonth) {
        if (payrollRepository.isNotTransferPending(storeId, targetMonth)) {
            throw new CustomException(CustomErrorInfo.PAYROLL_TRANSFER_ALREADY_STARTED);
        }
        payrollRepository.deleteAllByStoreIdAndMonth(storeId, targetMonth);
    }

    public List<PayrollEntity> getConfirmedPayroll(final Long storeId, final LocalDate month) {
        return payrollRepository.getAllByStoreIdAndMonth(storeId, month);
    }

    public List<PayrollEntity> getPayrollsByMonth(final Long storeId, final YearMonth yearMonth) {
        LocalDate today = LocalDate.now(clock).minusMonths(1);
        if (yearMonth.isBefore(YearMonth.from(today)) || hasStartedTransfer(storeId, yearMonth)) {
            return payrollRepository.findAllByStoreIdAndMonthBetween(
                    storeId,
                    yearMonth.atDay(1),
                    yearMonth.atEndOfMonth()
            );
        }
        throw new CustomException(CustomErrorInfo.PAYROLL_LOOKUP_TOO_EARLY);
    }

    public boolean hasStartedTransfer(final Long storeId, final YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        return payrollRepository.isTransferStarted(storeId, start, end);
    }

    public PayrollEntity getStorePayrollById(final Long storeId, final Long payrollId) {
        return payrollRepository.getById(payrollId);
    }
}
