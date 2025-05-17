package com.mangoboss.app.domain.service.payroll;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.PayrollSettingRepository;
import com.mangoboss.app.domain.repository.TransferAccountRepository;
import com.mangoboss.app.external.nhdevelopers.NhDevelopersClient;
import com.mangoboss.app.external.nhdevelopers.dto.response.CheckFinAccountResponse;
import com.mangoboss.app.external.nhdevelopers.dto.response.DepositorAccountNumberResponse;
import com.mangoboss.app.external.nhdevelopers.dto.response.FinAccountDirectResponse;
import com.mangoboss.storage.payroll.BankCode;
import com.mangoboss.storage.payroll.PayrollSettingEntity;
import com.mangoboss.storage.payroll.TransferAccountEntity;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayrollSettingService {
    private final PayrollSettingRepository payrollSettingRepository;
    private final TransferAccountRepository transferAccountRepository;
    private final NhDevelopersClient nhDevelopersClient;

    @Value("${external.nh.fin-acno}")
    private String finAcno;
    private final Clock clock;

    private void isBossAccount(final String bossName, final BankCode bankCode, final String accountNumber) {
        DepositorAccountNumberResponse response = nhDevelopersClient.getVerifyAccountHolder(bankCode.getCode(), accountNumber);
        String realName = response.Dpnm();
        if (!realName.equals(bossName)) {
            throw new CustomException(CustomErrorInfo.NOT_OWNER_ACCOUNT);
        }
    }

    private String registerFinAccount(final BankCode bankCode,
                                      final String accountNumber, final String birthDate) {
//        FinAccountDirectResponse finAccountDirectResponse =
//                nhDevelopersClient.finAccountDirect(bankCode.getCode(), accountNumber, birthDate);
//        CheckFinAccountResponse checkFinAccountResponse =
//                nhDevelopersClient.checkFinAccount(finAccountDirectResponse.Rgno(), birthDate);
//        checkFinAccountResponse.FinAcno();
        return finAcno;
    }

    @Transactional
    public TransferAccountEntity registerBossAccount(final StoreEntity store, final BankCode bankCode,
                                                     final String accountNumber, final String birthDate) {
        PayrollSettingEntity payrollSetting = store.getPayrollSetting();
        String bossName = store.getBoss().getName();

        isBossAccount(bossName, bankCode, accountNumber);
        String finAccount = registerFinAccount(bankCode, accountNumber, birthDate);
        TransferAccountEntity transferAccount = TransferAccountEntity.create(
                bankCode,
                bossName,
                accountNumber,
                finAccount
                );
        payrollSetting.registerTransferAccountEntity(transferAccount);
        return transferAccountRepository.save(transferAccount);
    }

    @Transactional
    public void initPayrollSettingForStore(final StoreEntity store) {
        PayrollSettingEntity payrollSetting = PayrollSettingEntity.init(store);
        payrollSettingRepository.save(payrollSetting);
    }

    @Transactional
    public void updatePayrollSettings(final PayrollSettingEntity payrollSetting,
                                      final Boolean autoTransferEnabled,
                                      final Integer transferDate,
                                      final Integer overtimeLimit,
                                      final Integer deductionUnit) {
        updateAutoTransferEnabled(payrollSetting, autoTransferEnabled, transferDate)
                .updatePayrollPolicy(overtimeLimit, deductionUnit);
    }

    private PayrollSettingEntity updateAutoTransferEnabled(final PayrollSettingEntity payrollSetting,
                                                           final Boolean autoTransferEnabled,
                                                           final Integer transferDate) {
        if (autoTransferEnabled && transferDate == null) {
            throw new CustomException(CustomErrorInfo.TRANSFER_DATE_REQUIRED);
        }
        if (autoTransferEnabled && payrollSetting.isTransferAccountNotSet()) {
            throw new CustomException(CustomErrorInfo.TRANSFER_ACCOUNT_REQUIRED);
        }
        return payrollSetting.updateAutoTransferEnabled(autoTransferEnabled, transferDate);
    }

}
