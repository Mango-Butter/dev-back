package com.mangoboss.app.domain.service.payroll;

import com.mangoboss.app.domain.repository.PayrollSettingRepository;
import com.mangoboss.app.domain.repository.TransferAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@ExtendWith(MockitoExtension.class)
class PayrollServiceTest {
    @Mock
    private PayrollSettingRepository payrollSettingRepository;

    @Mock
    private TransferAccountRepository transferAccountRepository;

    @Mock
    private NhDevelopersClient nhDevelopersClient;

    @InjectMocks
    private PayrollSettingService payrollService;

    private final LocalDateTime fixedNow = LocalDateTime.of(2025, 1, 1, 9, 0);
    private final Clock fixedClock = Clock.fixed(fixedNow.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @BeforeEach
    void setUp() {
        payrollService = new PayrollSettingService(payrollSettingRepository, transferAccountRepository, nhDevelopersClient, fixedClock);
    }

}