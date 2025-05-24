package com.mangoboss.app.domain.service.payroll;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.PayrollSettingRepository;
import com.mangoboss.app.domain.repository.TransferAccountRepository;
import com.mangoboss.app.external.nhdevelopers.NhDevelopersClient;
import com.mangoboss.storage.payroll.PayrollSettingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayrollSettingServiceTest {
    @Mock
    private PayrollSettingRepository payrollSettingRepository;

    @Mock
    private TransferAccountRepository transferAccountRepository;

    @Mock
    private NhDevelopersClient nhDevelopersClient;

    @InjectMocks
    private PayrollSettingService payrollSettingService;

    private final LocalDateTime fixedNow = LocalDateTime.of(2025, 1, 1, 9, 0);
    private final Clock fixedClock = Clock.fixed(fixedNow.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @BeforeEach
    void setUp() {
        payrollSettingService = new PayrollSettingService(payrollSettingRepository, transferAccountRepository, nhDevelopersClient, fixedClock);
    }

    @Test
    void 자동_송금_설정시_등록된_계좌가_없으면_예러를_던진다() {
        // given
        PayrollSettingEntity payrollSetting = mock(PayrollSettingEntity.class);
        when(payrollSetting.isTransferAccountNotSet()).thenReturn(true);

        // when
        // then
        assertThatThrownBy(() -> payrollSettingService.updatePayrollSettings(
                payrollSetting, true, 60, 10))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.TRANSFER_ACCOUNT_REQUIRED.getMessage());
    }

    @Test
    void 자동_송금_설정시_자동_송금일이_없으면_예러를_던진다() {
        // given
        PayrollSettingEntity payrollSetting = mock(PayrollSettingEntity.class);

        // when
        // then
        assertThatThrownBy(() -> payrollSettingService.updatePayrollSettings(
                payrollSetting, true, null, 10))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.TRANSFER_DATE_REQUIRED.getMessage());
    }

    @Test
    void 자동_송금이_꺼지면_언제든_정상_업데이트_된다() {
        // given
        PayrollSettingEntity payrollSetting = mock(PayrollSettingEntity.class);
        when(payrollSetting.updateAutoTransferEnabled(false, null)).thenReturn(payrollSetting);
        when(payrollSetting.updateDeductionUnit(10)).thenReturn(payrollSetting);

        // when
        payrollSettingService.updatePayrollSettings(
                payrollSetting, false, null, 10);

        // then
        verify(payrollSetting).updateAutoTransferEnabled(false, null);
        verify(payrollSetting).updateDeductionUnit(10);
    }
}