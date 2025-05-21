package com.mangoboss.app.api.facade.payroll;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.payroll.PayrollService;
import com.mangoboss.app.domain.service.payroll.PayrollSettingService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.payroll.request.AccountRegisterRequest;
import com.mangoboss.app.dto.payroll.request.PayrollSettingRequest;
import com.mangoboss.app.dto.payroll.response.AccountRegisterResponse;
import com.mangoboss.app.dto.payroll.response.PayrollEstimatedResponse;
import com.mangoboss.app.dto.payroll.response.PayrollSettingResponse;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.payroll.BankCode;
import com.mangoboss.storage.payroll.PayrollSettingEntity;
import com.mangoboss.storage.payroll.TransferAccountEntity;
import com.mangoboss.storage.payroll.estimated.EstimatedPayrollEntity;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BossPayrollFacade {
    private final StoreService storeService;
    private final StaffService staffService;
    private final PayrollSettingService payrollSettingService;
    private final PayrollService payrollService;
    private final AttendanceService attendanceService;

    private final Clock clock;

    public AccountRegisterResponse registerBossAccount(final Long storeId, final Long bossId, final AccountRegisterRequest request) {
        StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        BankCode bankCode = BankCode.findCodeByName(request.bankName())
                .orElseThrow(() -> new CustomException(CustomErrorInfo.INVALID_BANK_NAME));
        TransferAccountEntity transferAccount = payrollSettingService.registerBossAccount(store, bankCode, request.accountNumber(), request.birthdate());
        return AccountRegisterResponse.fromEntity(transferAccount);
    }

    public void updatePayrollSettings(final Long storeId, final Long bossId, final PayrollSettingRequest request) {
        StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        payrollSettingService.updatePayrollSettings(
                store.getPayrollSetting(),
                request.autoTransferEnabled(),
                request.transferDate(),
                request.overtimeLimit(),
                request.deductionUnit().getValue());
    }

    public PayrollSettingResponse getPayrollSettings(final Long storeId, final Long bossId) {
        StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        return PayrollSettingResponse.fromEntity(store.getPayrollSetting());
    }

    public List<PayrollEstimatedResponse> getEstimatedPayrollsForStaffs(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        PayrollSettingEntity payrollSetting = payrollSettingService.validateAutoTransferAndGetPayrollSetting(storeId);
        List<StaffEntity> staffs = staffService.getStaffsForStore(storeId);

        LocalDate today = LocalDate.now(clock);
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate startDate = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());

        List<EstimatedPayrollEntity> payrolls = staffs.stream().map(staff -> {
            List<AttendanceEntity> attendances = attendanceService.getAttendancesByStaffAndDateRange(
                    staff.getId(),
                    startDate,
                    endDate);
            return payrollService.createEstimatedPayroll(staff, payrollSetting, attendances, firstDayOfMonth);
        }).toList();

        return payrolls.stream()
                .map(estimated -> PayrollEstimatedResponse.of(estimated, staffService.getStaffById(estimated.getStaffId())))
                .toList();
    }
}

