package com.mangoboss.app.api.facade.payroll;

import com.mangoboss.app.domain.service.payroll.PayrollService;
import com.mangoboss.app.domain.service.payroll.PayslipService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.payroll.response.PayrollDetailResponse;
import com.mangoboss.app.dto.payroll.response.PayrollResponse;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class StaffPayrollFacade {
    private final StaffService staffService;
    private final PayrollService payrollService;
    private final PayslipService payslipService;


    public PayrollResponse getPayrollDetail(final Long storeId, final Long userId, final YearMonth yearMonth) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        payrollService.validateMonthIsBeforeCurrent(yearMonth);
        PayrollEntity payroll = payrollService.getPayrollForStaffAndMonth(staff.getId(), yearMonth);
        if (payroll != null) {
            return PayrollResponse.ofForPayroll(payroll, payslipService.getPayslipByPayrollId(payroll.getId()));
        }
        return PayrollResponse.ofForNoPayroll(payrollService.createEstimatedPayroll(
                staff,
                staff.getStore().getPayrollSetting(),
                yearMonth.atDay(1)
        ));
    }

    public DownloadPreSignedUrlResponse getPayrollDownloadUrl(final Long storeId, final Long userId, final Long payslipId) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        payslipService.verifyPayslipOwner(payslipId, staff.getId());
        return payslipService.getPayslipDownloadUrl(payslipId);
    }
}
