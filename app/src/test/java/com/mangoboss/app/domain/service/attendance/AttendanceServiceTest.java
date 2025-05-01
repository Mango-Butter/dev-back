package com.mangoboss.app.domain.service.attendance;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.AttendanceRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.attendance.ClockOutStatus;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
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

    @InjectMocks
    private AttendanceService attendanceService;

    private final LocalDateTime fixedNow = LocalDateTime.of(2025, 5, 2, 10, 0);
    private final Clock fixedClock = Clock.fixed(fixedNow.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @BeforeEach
    void setUp() {
        attendanceService = new AttendanceService(attendanceRepository, fixedClock);
    }

    @Test
    void 출근_정상_기록() {
        // given
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(schedule.getId()).thenReturn(1L);
        when(schedule.getStartTime()).thenReturn(fixedNow.minusMinutes(10));
        when(schedule.getEndTime()).thenReturn(fixedNow.plusHours(1));

        when(attendanceRepository.existsByScheduleId(1L)).thenReturn(false);
        when(attendanceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0)); // save mock

        // when
        AttendanceEntity attendance = attendanceService.recordClockIn(schedule);

        // then
        assertThat(attendance.getClockInStatus()).isEqualTo(ClockInStatus.LATE);
        assertThat(attendance.getClockInTime()).isEqualTo(fixedNow); // now가 startTime보다 이후
    }

    @Test
    void 이미_출근했으면_예외() {
        // given
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(schedule.getId()).thenReturn(1L);
        when(attendanceRepository.existsByScheduleId(1L)).thenReturn(true);

        // when
        // then
        assertThatThrownBy(() -> attendanceService.recordClockIn(schedule))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.ALREADY_CLOCKED_IN.getMessage());
    }

    @Test
    void 퇴근_정상_기록() {
        // given
        AttendanceEntity attendance = mock(AttendanceEntity.class);
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(schedule.getEndTime()).thenReturn(LocalDateTime.now().minusMinutes(1));
        when(attendance.getSchedule()).thenReturn(schedule);
        when(attendance.isAlreadyClockedOut()).thenReturn(false);

        // when
        attendanceService.recordClockOut(attendance);

        // then
        verify(attendance).recordClockOut(any(LocalDateTime.class), any(ClockOutStatus.class));
    }

    @Test
    void 조기퇴근은_EARLY_LEAVE로_기록된다() {
        // given
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(schedule.getEndTime()).thenReturn(fixedNow.plusMinutes(10));
        AttendanceEntity attendance = mock(AttendanceEntity.class);
        when(attendance.getSchedule()).thenReturn(schedule);
        when(attendance.isAlreadyClockedOut()).thenReturn(false);

        // when
        attendanceService.recordClockOut(attendance);

        // then
        verify(attendance).recordClockOut(eq(fixedNow), eq(ClockOutStatus.EARLY_LEAVE));
    }

    @Test
    void 연장근무는_OVERTIME으로_기록된다() {
        // given
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(schedule.getEndTime()).thenReturn(fixedNow.minusMinutes(15)); // 15분 초과
        AttendanceEntity attendance = mock(AttendanceEntity.class);
        when(attendance.getSchedule()).thenReturn(schedule);
        when(attendance.isAlreadyClockedOut()).thenReturn(false);

        // when
        attendanceService.recordClockOut(attendance);

        // then
        verify(attendance).recordClockOut(eq(fixedNow), eq(ClockOutStatus.OVERTIME));
    }

    @Test
    void 이미_퇴근했으면_예외() {
        // given
        AttendanceEntity attendance = mock(AttendanceEntity.class);
        when(attendance.isAlreadyClockedOut()).thenReturn(true);

        // when
        // then
        assertThatThrownBy(() -> attendanceService.recordClockOut(attendance))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.ALREADY_CLOCKED_OUT.getMessage());
    }

    @Test
    void 근태기록이_알바생과_일치하면_성공() {
        // given
        StaffEntity staff = mock(StaffEntity.class);
        AttendanceEntity attendance = mock(AttendanceEntity.class);
        ScheduleEntity schedule = mock(ScheduleEntity.class);

        when(attendance.getSchedule()).thenReturn(schedule);
        when(schedule.getStaff()).thenReturn(staff);

        // when
        // then
        assertThatNoException().isThrownBy(() ->
                attendanceService.validateAttendanceBelongsToStaff(attendance, staff));
    }

    @Test
    void 근태기록이_알바생과_불일치하면_예외() {
        // given
        StaffEntity staff = mock(StaffEntity.class);
        StaffEntity anotherStaff = mock(StaffEntity.class);
        AttendanceEntity attendance = mock(AttendanceEntity.class);
        ScheduleEntity schedule = mock(ScheduleEntity.class);

        when(attendance.getSchedule()).thenReturn(schedule);
        when(schedule.getStaff()).thenReturn(anotherStaff);

        // when
        // then
        assertThatThrownBy(() ->
                attendanceService.validateAttendanceBelongsToStaff(attendance, staff))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.ATTENDANCE_NOT_BELONG_TO_STAFF.getMessage());
    }

    @Test
    void 스케줄_종료_이후_출근시도시_예외() {
        // given
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(schedule.getId()).thenReturn(1L);

        when(schedule.getEndTime()).thenReturn(fixedNow.minusMinutes(10));

        when(attendanceRepository.existsByScheduleId(1L)).thenReturn(false);

        // when
        // then
        assertThatThrownBy(() -> attendanceService.recordClockIn(schedule))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.SCHEDULE_ALREADY_ENDED.getMessage());
    }
}