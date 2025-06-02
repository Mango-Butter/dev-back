package com.mangoboss.app.api.facade.staff;

import com.mangoboss.app.domain.service.payroll.PayrollSettingService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.payroll.response.AccountRegisterResponse;
import com.mangoboss.app.dto.staff.request.StaffAccountRegisterRequest;
import com.mangoboss.app.dto.staff.request.StaffInfoResponse;
import com.mangoboss.storage.payroll.BankCode;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffStaffFacade {
    private final StaffService staffService;
    private final PayrollSettingService payrollSettingService;

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
}
