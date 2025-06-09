package com.mangoboss.app.api.facade.payroll;

import com.mangoboss.app.domain.service.cache.CacheService;
import com.mangoboss.app.domain.service.payroll.EstimatedPayroll;
import com.mangoboss.app.domain.service.payroll.PayrollService;
import com.mangoboss.app.domain.service.payroll.PayrollSettingService;
import com.mangoboss.app.domain.service.payroll.PayslipService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.payroll.request.AccountRegisterRequest;
import com.mangoboss.app.dto.payroll.request.ConfirmEstimatedPayrollRequest;
import com.mangoboss.app.dto.payroll.request.PayrollSettingRequest;
import com.mangoboss.app.dto.payroll.response.*;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.storage.payroll.*;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BossPayrollFacade {
    private final StoreService storeService;
    private final StaffService staffService;

    private final PayrollSettingService payrollSettingService;
    private final PayrollService payrollService;
    private final PayslipService payslipService;
    private final CacheService cacheService;

    private final Clock clock;

    public AccountRegisterResponse registerBossAccount(final Long storeId, final Long bossId, final AccountRegisterRequest request) {
        StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        BankCode bankCode = payrollSettingService.validateBankName(request.bankName());
        TransferAccountEntity transferAccount = payrollSettingService.registerBossAccount(store, bankCode, request.accountNumber(), request.birthdate());
        return AccountRegisterResponse.fromEntity(transferAccount);
    }

    public void updatePayrollSettings(final Long storeId, final Long bossId, final PayrollSettingRequest request) {
        StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        payrollSettingService.updatePayrollSettings(
                store.getPayrollSetting(),
                request.autoTransferEnabled(),
                request.transferDate(),
                request.deductionUnit().getValue(),
                request.commutingAllowance()
        );
    }

    public PayrollSettingResponse getPayrollSettings(final Long storeId, final Long bossId) {
        StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        return PayrollSettingResponse.fromEntity(store.getPayrollSetting());
    }

    public List<PayrollEstimatedWithStaffResponse> getEstimatedPayrolls(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        PayrollSettingEntity payrollSetting = payrollSettingService.validateAutoTransferAndGetPayrollSetting(storeId);
        List<StaffEntity> staffs = staffService.getStaffsForStore(storeId);

        LocalDate targetMonth = LocalDate.now(clock).withDayOfMonth(1).minusMonths(1);
        List<EstimatedPayroll> payrolls = staffs.stream().map(staff ->
                payrollService.createEstimatedPayroll(staff, payrollSetting, targetMonth)).toList();
        cacheService.cacheObject(payrollService.generateEstimatedPayrollKey(storeId, targetMonth), payrolls, 20);

        return payrolls.stream()
                .map(estimated -> PayrollEstimatedWithStaffResponse.of(estimated, staffService.getStaffById(estimated.getStaffId())))
                .toList();
    }

    public void confirmEstimatedPayroll(final Long storeId, final Long bossId, final ConfirmEstimatedPayrollRequest request) {
        StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        LocalDate targetMonth = LocalDate.now(clock).withDayOfMonth(1).minusMonths(1);

        PayrollSettingEntity payrollSetting = payrollSettingService.validateAutoTransferAndGetPayrollSetting(storeId);

        String key = payrollService.generateEstimatedPayrollKey(storeId, targetMonth);
        List<EstimatedPayroll> cached = cacheService.getCachedObject(key, EstimatedPayroll.class);
        payrollService.deletePayrollsByStoreIdAndMonth(storeId, targetMonth);
        payrollService.confirmEstimatedPayroll(store, payrollSetting, cached, request.payrollKeys());
    }


    public List<PayrollWithStaffResponse> getConfirmedPayrolls(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        LocalDate targetMonth = LocalDate.now(clock).withDayOfMonth(1).minusMonths(1);

//        payrollSettingService.validateAutoTransferAndGetPayrollSetting(storeId);
        List<PayrollEntity> confirmedPayroll = payrollService.getPayrolls(storeId, targetMonth);
        return confirmedPayroll.stream()
                .map(payroll -> PayrollWithStaffResponse.ofForPayroll(
                        staffService.getStaffById(payroll.getStaffId()),
                        payroll,
                        payslipService.getPayslipByPayrollId(payroll.getId())))
                .toList();
    }

    public void deleteAccount(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        payrollSettingService.deleteAccount(storeId);
    }

    public List<PayrollWithStaffResponse> getPayrollsByMonth(final Long storeId, final Long bossId, final YearMonth yearMonth) {
        storeService.isBossOfStore(storeId, bossId);
//        payrollSettingService.isTransferDateBefore(storeId, yearMonth);
//        payrollService.validateMonthIsBeforeCurrent(yearMonth);

        List<StaffEntity> staffs = staffService.getStaffsForStore(storeId);
        List<PayrollEntity> payrolls = payrollService.getTransferredPayrollsByMonth(storeId, yearMonth);
        Map<Long, PayrollEntity> payrollMap = payrolls.stream()
                .collect(Collectors.toMap(PayrollEntity::getStaffId, p -> p));
        PayrollSettingEntity payrollSetting = payrollSettingService.getPayrollSettingByStoreId(storeId);

        return staffs.stream().map(staff -> {
            PayrollEntity payroll = payrollMap.get(staff.getId());
            if (payroll != null) {
                return PayrollWithStaffResponse.ofForPayroll(
                        staff,
                        payroll,
                        payslipService.getPayslipByPayrollId(payroll.getId())
                );
            }
            EstimatedPayroll estimated = payrollService.createEstimatedPayroll(staff, payrollSetting, yearMonth.atDay(1));
            return PayrollWithStaffResponse.ofForNoPayroll(staff, estimated);
        }).toList();
    }

    public PayrollResponse getPayroll(final Long storeId, final Long payrollId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        return PayrollResponse.ofForPayroll(
                payrollService.getStorePayrollById(storeId, payrollId),
                payslipService.getPayslipByPayrollId(payrollId)
        );
    }

    public DownloadPreSignedUrlResponse getPayslipDownloadUrl(final Long storeId, final Long payslipId, final Long userId) {
        storeService.isBossOfStore(storeId, userId);
        return payslipService.getPayslipDownloadUrl(payslipId);
    }

    public PayrollResponse getEstimatedPayrollForStaff(final Long storeId, final Long staffId, final Long bossId, final YearMonth yearMonth) {
        storeService.isBossOfStore(storeId, bossId);
        payrollService.validateMonthIsBeforeCurrent(yearMonth);
        EstimatedPayroll estimatedPayroll = payrollService.createEstimatedPayroll(
                staffService.getStaffById(staffId),
                payrollSettingService.validateAutoTransferAndGetPayrollSetting(storeId),
                yearMonth.atDay(1)
        );
        return PayrollResponse.ofForNoPayroll(estimatedPayroll);
    }

    public TransferSummaryResponse getPayrollSummary(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        PayrollSettingEntity payrollSetting = payrollSettingService.getPayrollSettingByStoreId(storeId);
        LocalDate targetMonth = LocalDate.now(clock).withDayOfMonth(1).minusMonths(1);
        LocalDate today = LocalDate.now(clock);
        List<PayrollEntity> payrolls = payrollService.getPayrolls(storeId, targetMonth);
        if (payrolls.isEmpty()) {
            return TransferSummaryResponse.of(TransferSummaryStateForResponse.NOT_YET, today.withDayOfMonth(payrollSetting.getTransferDate()));
        }
        boolean allPending = payrolls.stream().allMatch(p ->
                p.getTransferState().equals(TransferState.PENDING)
        );
        if (allPending){
            return TransferSummaryResponse.of(TransferSummaryStateForResponse.PENDING, payrolls.get(0).getTransferDate());
        }
        return TransferSummaryResponse.of(TransferSummaryStateForResponse.COMPLETED, payrolls.get(0).getTransferDate());
    }
}

