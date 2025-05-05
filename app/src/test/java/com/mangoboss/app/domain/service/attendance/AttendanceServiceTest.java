package com.mangoboss.app.domain.service.attendance;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.AttendanceRepository;
import com.mangoboss.app.domain.repository.ScheduleRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.attendance.ClockOutStatus;
import com.mangoboss.storage.schedule.ScheduleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private final LocalDateTime fixedNow = LocalDateTime.of(2025, 5, 2, 10, 0);
    private final Clock fixedClock = Clock.fixed(fixedNow.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    ScheduleEntity schedule = mock(ScheduleEntity.class);
    Long staffId = 1L;
    AttendanceEntity attendance = mock(AttendanceEntity.class);

    @BeforeEach
    void setUp() {
        attendanceService = new AttendanceService(attendanceRepository, scheduleRepository, fixedClock);
    }

    @Test
    void 출근_정상_기록() {
        // given
        when(schedule.getStartTime()).thenReturn(fixedNow.minusMinutes(10));
        when(schedule.getEndTime()).thenReturn(fixedNow.plusHours(1));

        when(scheduleRepository.getByIdAndStaffId(schedule.getId(), staffId)).thenReturn(schedule);
        when(attendanceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0)); // save mock

        // when
        AttendanceEntity attendance = attendanceService.recordClockIn(staffId, schedule.getId());

        // then
        assertThat(attendance.getClockInStatus()).isEqualTo(ClockInStatus.LATE);
        assertThat(attendance.getClockInTime()).isEqualTo(fixedNow);
    }

    @Test
    void 이미_출근했으면_예외() {
        // given
        when(scheduleRepository.getByIdAndStaffId(schedule.getId(), staffId)).thenReturn(schedule);
        when(schedule.getAttendance()).thenReturn(attendance);

        // when
        // then
        assertThatThrownBy(() -> attendanceService.recordClockIn(staffId, schedule.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.ALREADY_CLOCKED_IN.getMessage());
    }

    @Test
    void 퇴근_정상_기록() {
        // given
        when(schedule.getEndTime()).thenReturn(LocalDateTime.now().minusMinutes(1));
        when(schedule.getAttendance()).thenReturn(attendance);
        when(attendance.isAlreadyClockedOut()).thenReturn(false);
        when(scheduleRepository.getByIdAndStaffId(schedule.getId(), staffId)).thenReturn(schedule);

        // when
        attendanceService.recordClockOut(staffId, schedule.getId());

        // then
        verify(attendance).recordClockOut(any(LocalDateTime.class), any(ClockOutStatus.class));
    }

    @Test
    void 조기퇴근은_EARLY_LEAVE로_기록된다() {
        // given
        when(schedule.getEndTime()).thenReturn(fixedNow.plusMinutes(10));
        when(schedule.getAttendance()).thenReturn(attendance);
        when(attendance.isAlreadyClockedOut()).thenReturn(false);
        when(scheduleRepository.getByIdAndStaffId(schedule.getId(), staffId)).thenReturn(schedule);

        // when
        attendanceService.recordClockOut(staffId, schedule.getId());

        // then
        verify(attendance).recordClockOut(eq(fixedNow), eq(ClockOutStatus.EARLY_LEAVE));
    }

    @Test
    void 연장근무는_OVERTIME으로_기록된다() {
        // given
        when(schedule.getEndTime()).thenReturn(fixedNow.minusMinutes(15)); // 15분 초과
        when(schedule.getAttendance()).thenReturn(attendance);
        when(attendance.isAlreadyClockedOut()).thenReturn(false);
        when(scheduleRepository.getByIdAndStaffId(schedule.getId(), staffId)).thenReturn(schedule);

        // when
        attendanceService.recordClockOut(staffId, schedule.getId());

        // then
        verify(attendance).recordClockOut(eq(fixedNow), eq(ClockOutStatus.OVERTIME));
    }

    @Test
    void 이미_퇴근했으면_예외() {
        // given
        when(schedule.getAttendance()).thenReturn(attendance);
        when(attendance.isAlreadyClockedOut()).thenReturn(true);
        when(scheduleRepository.getByIdAndStaffId(schedule.getId(), staffId)).thenReturn(schedule);

        // when
        // then
        assertThatThrownBy(() -> attendanceService.recordClockOut(staffId, schedule.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.ALREADY_CLOCKED_OUT.getMessage());
    }


    @Test
    void 스케줄_종료_이후_출근시도시_예외() {
        // given
        when(schedule.getAttendance()).thenReturn(null);
        when(schedule.getEndTime()).thenReturn(fixedNow.minusMinutes(1));
        when(scheduleRepository.getByIdAndStaffId(schedule.getId(), staffId)).thenReturn(schedule);

        // when
        // then
        assertThatThrownBy(() -> attendanceService.recordClockIn(staffId, schedule.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.SCHEDULE_ALREADY_ENDED.getMessage());
    }
}