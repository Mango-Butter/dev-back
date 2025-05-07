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

import java.time.*;

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
    AttendanceEntity attendance = mock(AttendanceEntity.class);

    @BeforeEach
    void setUp() {
        attendanceService = new AttendanceService(attendanceRepository, scheduleRepository, fixedClock);
    }

    @Test
    void 출근_정상_기록() {
        // given
        Long staffId = 1L;
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
        Long staffId = 1L;
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
        Long staffId = 1L;
        when(schedule.getEndTime()).thenReturn(fixedNow.withSecond(0).withNano(0));
        when(schedule.getAttendance()).thenReturn(attendance);
        when(attendance.isAlreadyClockedOut()).thenReturn(false);
        when(scheduleRepository.getByIdAndStaffId(schedule.getId(), staffId)).thenReturn(schedule);

        // when
        attendanceService.recordClockOut(staffId, schedule.getId());

        // then
        verify(attendance).recordClockOut(eq(fixedNow), eq(ClockOutStatus.NORMAL));
    }

    @Test
    void 조기퇴근은_EARLY_LEAVE로_기록된다() {
        // given
        Long staffId = 1L;
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
        Long staffId = 1L;
        when(schedule.getEndTime()).thenReturn(fixedNow.minusMinutes(15));
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
        Long staffId = 1L;
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
        Long staffId = 1L;
        when(schedule.getAttendance()).thenReturn(null);
        when(schedule.getEndTime()).thenReturn(fixedNow.minusMinutes(1));
        when(scheduleRepository.getByIdAndStaffId(schedule.getId(), staffId)).thenReturn(schedule);

        // when
        // then
        assertThatThrownBy(() -> attendanceService.recordClockIn(staffId, schedule.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.SCHEDULE_ALREADY_ENDED.getMessage());
    }

    @Test
    void 과거의_날짜에만_근태기록을_수동으로_추가_할_수_있다() {
        // given
        LocalDate workDate = fixedNow.toLocalDate().minusDays(1);

        // when
        // then
        assertThatNoException().isThrownBy(() -> attendanceService.validateWorkDateForManualAttendance(workDate));
    }

    @Test
    void 미래의_날짜에_근태기록을_수동으로_추가하면_에러가_발생한다() {
        // given
        LocalDate workDate = fixedNow.toLocalDate();

        // when
        // then
        assertThatThrownBy(() -> attendanceService.validateWorkDateForManualAttendance(workDate))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.ATTENDANCE_DATE_MUST_BE_PAST.getMessage());
    }

    @Test
    void 근태기록을_수동으로_추가하면_출근상태는_NORMAL이다() {
        // given
        when(attendanceRepository.save(any(AttendanceEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //when
        AttendanceEntity result = attendanceService.createManualAttendanceAndSchedule(schedule);

        // then
        assertThat(result.getClockInStatus()).isEqualTo(ClockInStatus.NORMAL);
    }

    @Test
    void 근태기록을_수정할_수_있다() {
        LocalDateTime originalEndTime = LocalDateTime.of(2024, 5, 5, 15, 0);
        AttendanceEntity attendance = AttendanceEntity.builder()
                .schedule(schedule)
                .clockInStatus(ClockInStatus.LATE)
                .clockOutStatus(ClockOutStatus.NORMAL)
                .build();
        when(schedule.getEndTime()).thenReturn(originalEndTime);
        when(schedule.getAttendance()).thenReturn(attendance);
        when(attendanceRepository.getByScheduleId(any())).thenReturn(attendance);

        LocalDateTime clockInTime = LocalDateTime.of(2024, 5, 5, 9, 0);
        LocalDateTime clockOutTime = LocalDateTime.of(2024, 5, 5, 15, 0);
        ClockInStatus clockInStatus = ClockInStatus.NORMAL;

        // when
        AttendanceEntity result = attendanceService.updateAttendance(schedule, clockInTime, clockOutTime, clockInStatus);

        // then
        assertThat(result.getClockInTime()).isEqualTo(clockInTime);
        assertThat(result.getClockOutTime()).isEqualTo(clockOutTime);
        assertThat(result.getClockInStatus()).isEqualTo(clockInStatus);
        assertThat(result.getClockOutStatus()).isEqualTo(ClockOutStatus.NORMAL);
    }

    @Test
    void 완성되지_않은_근태기록을_수정하면_에러를_던진다() {
        // given
        Long scheduleId = 1L;
        AttendanceEntity attendance = AttendanceEntity.builder()
                .clockOutStatus(null)
                .build();

        LocalDateTime clockInTime = LocalDateTime.of(2024, 5, 5, 9, 0);
        LocalDateTime clockOutTime = LocalDateTime.of(2024, 5, 5, 15, 0);
        ClockInStatus clockInStatus = ClockInStatus.NORMAL;

        when(attendanceRepository.getByScheduleId(scheduleId)).thenReturn(attendance);

        // when
        // then
        assertThatThrownBy(() -> attendanceService.updateAttendance(schedule, clockInTime, clockOutTime, clockInStatus));
    }
}