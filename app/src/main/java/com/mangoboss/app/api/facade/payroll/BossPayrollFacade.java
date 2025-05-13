package com.mangoboss.app.api.facade.payroll;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.service.payroll.PayrollService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.app.dto.payroll.request.AccountRegisterRequest;
import com.mangoboss.app.dto.payroll.response.AccountRegisterResponse;
import com.mangoboss.storage.payroll.BankCode;
import com.mangoboss.storage.payroll.TransferAccountEntity;
import com.mangoboss.storage.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BossPayrollFacade {
    private final StoreService storeService;
    private final PayrollService payrollService;
    private final UserService userService;

    public AccountRegisterResponse registerBossAccount(final Long storeId, final Long bossId, final AccountRegisterRequest request) {
        final UserEntity boss = userService.getUserById(bossId);
        storeService.isBossOfStore(storeId, bossId);

        final BankCode bankCode = BankCode.findCodeByName(request.bankName())
                        .orElseThrow(()->new CustomException(CustomErrorInfo.INVALID_BANK_NAME));
        final TransferAccountEntity transferAccount = payrollService.registerBossAccount(boss.getName(), bankCode, request.accountNumber());
        return AccountRegisterResponse.fromEntity(transferAccount);
    }
}
