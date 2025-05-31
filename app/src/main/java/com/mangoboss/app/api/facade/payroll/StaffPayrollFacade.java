package com.mangoboss.app.api.facade.payroll;

import com.mangoboss.app.domain.service.payroll.PayrollService;
import com.mangoboss.app.domain.service.payroll.PayslipService;
import com.mangoboss.app.domain.service.staff.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffPayrollFacade {
    private final StaffService staffService;
    private final PayrollService payrollService;
    private final PayslipService payslipService;


//    public PayrollWithPayslipResponse getPayrollDetail(final Long storeId, final Long userId, final YearMonth yearMonth) {
//        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
//        PayrollEntity payroll = payrollService.getPayrollForStaffAndMonth(staff.getId(), yearMonth);
//
//        return PayrollWithPayslipResponse.of(payroll, payslipService.getPayslipByPayrollId(payroll.getId()));
//    }
}
