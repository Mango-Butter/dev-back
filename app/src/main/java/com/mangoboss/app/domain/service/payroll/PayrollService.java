package com.mangoboss.app.domain.service.payroll;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.PayrollSettingRepository;
import com.mangoboss.app.domain.repository.TransferAccountRepository;
import com.mangoboss.storage.payroll.BankCode;
import com.mangoboss.storage.payroll.TransferAccountEntity;
import com.mangoboss.storage.payroll.TransferAccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayrollService {
    private final PayrollSettingRepository payrollSettingRepository;
    private final TransferAccountRepository transferAccountRepository;
    private final NhDevelopersClient nhDevelopersClient;

    public void isBossAccount(final String bossName, final BankCode bankCode, final String accountNumber) {
        final String realName = nhDevelopersClient.getVerifyAccountHolder(bankCode.getCode(), accountNumber);
        if (!realName.equals(bossName)) {
            throw new CustomException(CustomErrorInfo.NOT_OWNER_ACCOUNT);
        }
    }

    public TransferAccountEntity registerBossAccount(final String bossName, final BankCode bankCode, final String accountNumber) {
        isBossAccount(bossName, bankCode, accountNumber);
        final TransferAccountEntity transferAccount = TransferAccountEntity.create(
                bankCode,
                bossName,
                accountNumber
        );
        return transferAccountRepository.save(transferAccount);
    }
}
