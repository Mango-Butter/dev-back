package com.mangoboss.app.api.facade.staff;

import com.mangoboss.app.domain.service.payroll.PayrollService;
import com.mangoboss.app.domain.service.payroll.PayrollSettingService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.payroll.response.AccountRegisterResponse;
import com.mangoboss.app.dto.staff.request.StaffAccountRegisterRequest;
import com.mangoboss.app.dto.staff.request.StaffInfoResponse;
import com.mangoboss.storage.payroll.BankCode;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayrollSettingEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StaffStaffFacade {
    private final StaffService staffService;
    private final PayrollSettingService payrollSettingService;
    private final PayrollService payrollService;
    private final Clock clock;

    public AccountRegisterResponse registerStaffAccount(final Long storeId, final Long userId, final StaffAccountRegisterRequest request) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        BankCode bankCode = payrollSettingService.validateBankName(request.bankName());
        payrollSettingService.validateAccount(bankCode, request.accountNumber());
        staffService.registerStaffAccount(staff, bankCode, request.accountNumber());
        return AccountRegisterResponse.fromStaffEntity(staff);
    }

    public AccountRegisterResponse getStaffAccount(final Long storeId, final Long userId) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        return AccountRegisterResponse.fromStaffEntity(staff);
    }

    public StaffInfoResponse getStaffInfo(final Long storeId, final Long userId) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        return StaffInfoResponse.fromEntity(staff);
    }

    public void deleteAccount(final Long storeId, final Long userId) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        payrollService.validateNoPendingPayroll(staff.getId());
        staffService.deleteAccount(staff);
    }
}
