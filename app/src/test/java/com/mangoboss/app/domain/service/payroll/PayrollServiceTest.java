package com.mangoboss.app.domain.service.payroll;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.AttendanceRepository;
import com.mangoboss.app.domain.repository.PayrollRepository;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayrollSettingEntity;
import com.mangoboss.storage.store.StoreEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PayrollServiceTest {
    @Mock
    private PayrollRepository payrollRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private PayrollService payrollService;

    private final LocalDateTime fixedNow = LocalDateTime.of(2025, 1, 1, 9, 0);
    private final Clock fixedClock = Clock.fixed(fixedNow.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    private final StoreEntity store = mock(StoreEntity.class);
    private final PayrollSettingEntity payrollSetting = mock(PayrollSettingEntity.class);

    @BeforeEach
    void setUp() {
        payrollService = new PayrollService(payrollRepository, attendanceRepository, fixedClock);
    }

    @Test
    void 예상_급여내역을_확정할_수_있다() {
        // given
        EstimatedPayroll ep1 = mock(EstimatedPayroll.class);
        EstimatedPayroll ep2 = mock(EstimatedPayroll.class);
        PayrollEntity payroll1 = mock(PayrollEntity.class);
        PayrollEntity payroll2 = mock(PayrollEntity.class);
        when(ep1.getKey()).thenReturn("k1");
        when(ep2.getKey()).thenReturn("k2");
        when(ep1.createPayroll(store, payrollSetting)).thenReturn(payroll1);
        when(ep2.createPayroll(store, payrollSetting)).thenReturn(payroll2);
        when(payrollRepository.saveAll(anyList())).thenReturn(List.of(payroll1, payroll2));

        // when
        List<PayrollEntity> result = payrollService.confirmEstimatedPayroll(
                store,
                payrollSetting,
                List.of(ep1, ep2),
                List.of("k1", "k2")
        );

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(payroll1);
        assertThat(result.get(1)).isEqualTo(payroll2);
    }

    @Test
    void 예상_급여를_새로_확정하기_전에_확정되었던_급여내역을_지울_수_있다() {
        // given
        Long storeId = 1L;
        LocalDate month = LocalDate.of(2024, 6, 1);
        when(payrollRepository.isNotTransferPending(storeId, month)).thenReturn(false);

        // when
        payrollService.deletePayrollsByStoreIdAndMonth(storeId, month);

        // then
        verify(payrollRepository).deleteAllByStoreIdAndMonth(storeId, month);
    }

    @Test
    void 예상_급여를_새로_확정하기_전에_이미_송금되었으면_에러를_던진다() {
        // given
        Long storeId = 1L;
        LocalDate month = LocalDate.of(2024, 6, 1);
        when(payrollRepository.isNotTransferPending(storeId, month)).thenReturn(true);

        // when
        // then
        assertThatThrownBy(() -> payrollService.deletePayrollsByStoreIdAndMonth(storeId, month))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.PAYROLL_TRANSFER_ALREADY_STARTED.getMessage());
    }

    @Test
    void 미래의_급여내역을_조회하는지_검증할_수_있다() {
        // given
        YearMonth current = YearMonth.from(fixedNow).minusMonths(1);

        // when & then
        assertThatNoException()
                .isThrownBy(() -> payrollService.validateMonthIsBeforeCurrent(current));
    }

    @Test
    void 이번달을_포함한_미래의_급여내역을_조회하면_에러를_던진다() {
        // given
        YearMonth current = YearMonth.from(fixedNow);

        // when & then
        assertThatThrownBy(()-> payrollService.validateMonthIsBeforeCurrent(current))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.PAYROLL_LOOKUP_TOO_EARLY.getMessage());
    }

    @Test
    void 알바생이_송금_예정되어있는_급여가_없때는_계좌를_삭제할_수_있다() {
        // given
        Long staffId = 1L;
        YearMonth lastMonth = YearMonth.from(fixedNow).minusMonths(1);

        when(payrollRepository.getByStaffIdAndMonthBetween(
                eq(staffId),
                eq(lastMonth.atDay(1)),
                eq(lastMonth.atEndOfMonth())
        )).thenReturn(null);

        // when
        // then
        assertThatNoException()
                .isThrownBy(() -> payrollService.validateNoPendingPayroll(staffId));
    }

    @Test
    void 알바생이_송금_예정되어있는_급여가_있을때는_에러를_던진다() {
        // given
        Long staffId = 1L;
        YearMonth lastMonth = YearMonth.from(fixedNow).minusMonths(1);
        PayrollEntity payroll = mock(PayrollEntity.class);
        when(payroll.isPending()).thenReturn(true);

        when(payrollRepository.getByStaffIdAndMonthBetween(
                eq(staffId),
                eq(lastMonth.atDay(1)),
                eq(lastMonth.atEndOfMonth())
        )).thenReturn(payroll);

        // when
        // then
        assertThatThrownBy(() -> payrollService.validateNoPendingPayroll(staffId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(CustomErrorInfo.ACCOUNT_HAS_PENDING_TRANSFER.getMessage());
    }

    @Test
    void 급여내역을_알바생별로_조회할_수_있다() {
        // given
        Long staffId = 1L;
        YearMonth month = YearMonth.of(2024, 5);
        PayrollEntity payroll = mock(PayrollEntity.class);

        when(payrollRepository.getByStaffIdAndMonthBetween(
                eq(staffId),
                eq(month.atDay(1)),
                eq(month.atEndOfMonth())
        )).thenReturn(payroll);

        // when
        PayrollEntity result = payrollService.getPayrollForStaffAndMonth(staffId, month);

        // then
        assertThat(result).isEqualTo(payroll);
    }

    @Test
    void 매장별로_급여내역을_조회할_수_있다() {
        // given
        Long storeId = 1L;
        Long payrollId = 10L;
        PayrollEntity payroll = mock(PayrollEntity.class);

        when(payrollRepository.getByIdAndStoreId(payrollId, storeId)).thenReturn(payroll);

        // when
        PayrollEntity result = payrollService.getStorePayrollById(storeId, payrollId);

        // then
        assertThat(result).isEqualTo(payroll);
        verify(payrollRepository).getByIdAndStoreId(payrollId, storeId);
    }

    @Test
    void 레디스에_들어갈_예상급여내역_키를_만들_수_있다() {
        // given
        Long storeId = 123L;
        LocalDate month = LocalDate.of(2024, 5, 1); // 아무 날짜나 OK

        // when
        String result = payrollService.generateEstimatedPayrollKey(storeId, month);

        // then
        assertThat(result).isEqualTo("payroll:123:2024-05");
    }
}