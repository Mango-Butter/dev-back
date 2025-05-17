package com.mangoboss.app.api.facade.payroll;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.service.payroll.PayrollSettingService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.app.dto.payroll.request.AccountRegisterRequest;
import com.mangoboss.app.dto.payroll.request.PayrollSettingRequest;
import com.mangoboss.app.dto.payroll.response.AccountRegisterResponse;
import com.mangoboss.app.dto.payroll.response.PayrollSettingResponse;
import com.mangoboss.storage.payroll.BankCode;
import com.mangoboss.storage.payroll.TransferAccountEntity;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BossPayrollFacade {
    private final StoreService storeService;
    private final PayrollSettingService payrollSettingService;
    private final UserService userService;

    public AccountRegisterResponse registerBossAccount(final Long storeId, final Long bossId, final AccountRegisterRequest request) {
        final StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        final BankCode bankCode = BankCode.findCodeByName(request.bankName())
                .orElseThrow(() -> new CustomException(CustomErrorInfo.INVALID_BANK_NAME));
        final TransferAccountEntity transferAccount = payrollSettingService.registerBossAccount(store, bankCode, request.accountNumber(), request.birthdate());
        return AccountRegisterResponse.fromEntity(transferAccount);
    }

    public void updatePayrollSettings(final Long storeId, final Long bossId, final PayrollSettingRequest request) {
        final StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        payrollSettingService.updatePayrollSettings(
                store.getPayrollSetting(),
                request.autoTransferEnabled(),
                request.transferDate(),
                request.overtimeLimit(),
                request.deductionUnit().getValue());
    }

    public PayrollSettingResponse getPayrollSettings(final Long storeId, final Long userId) {
        final StoreEntity store = storeService.isBossOfStore(storeId, userId);
        return PayrollSettingResponse.fromEntity(store.getPayrollSetting());
    }
}

