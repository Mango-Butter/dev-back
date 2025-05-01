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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @Test
    void 출근_정상_기록() {
        // given
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        LocalDateTime startTime = LocalDateTime.of(2025, 5, 1, 9, 0);
        LocalDateTime endTime = startTime.plusHours(8); // 예: 17:00
        when(schedule.getId()).thenReturn(1L);
        when(schedule.getStartTime()).thenReturn(startTime);
        when(schedule.getEndTime()).thenReturn(endTime);
        when(attendanceRepository.existsByScheduleId(1L)).thenReturn(false);

        // when
        attendanceService.recordClockIn(schedule);

        // then
        verify(attendanceRepository).save(any(AttendanceEntity.class));
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
    void 출퇴근_방식_일치하면_성공() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        when(store.getAttendanceMethod()).thenReturn(AttendanceMethod.GPS);

        // when
        // then
        assertThatNoException().isThrownBy(() ->
                attendanceService.validateMethodConsistency(store, AttendanceMethod.GPS));
    }

    @Test
    void 출퇴근_방식_불일치시_예외() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        when(store.getAttendanceMethod()).thenReturn(AttendanceMethod.QR);

        // when
        // then
        assertThatThrownBy(() ->
                attendanceService.validateMethodConsistency(store, AttendanceMethod.GPS))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.ATTENDANCE_METHOD_CHANGED.getMessage());
    }

    @Test
    void QR_코드_일치시_성공() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        when(store.getQrCode()).thenReturn("XYZ123");

        // when
        // then
        assertThatNoException().isThrownBy(() ->
                attendanceService.validateQr(store, "XYZ123"));
    }

    @Test
    void QR_코드_불일치시_예외() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        when(store.getQrCode()).thenReturn("XYZ123");

        // when
        // then
        assertThatThrownBy(() ->
                attendanceService.validateQr(store, "INVALID"))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.INVALID_QR_CODE.getMessage());
    }

    @Test
    void GPS_시간이_유효범위를_벗어나면_예외() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        LocalDateTime fetchedAt = LocalDateTime.now().minusSeconds(30); // 유효 시간 초과

        // when
        // then
        assertThatThrownBy(() ->
                attendanceService.validateGps(store, 37.0, 127.0, fetchedAt))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.INVALID_GPS_TIME.getMessage());
    }

    @Test
    void GPS_거리가_반경을_초과하면_예외() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        when(store.getGpsLatitude()).thenReturn(37.0);
        when(store.getGpsLongitude()).thenReturn(127.0);
        when(store.getGpsRangeMeters()).thenReturn(10); // 10m 반경

        LocalDateTime fetchedAt = LocalDateTime.now();
        // 대략 50m 정도 떨어진 위치
        double lat = 37.0005;
        double lng = 127.0005;

        // when
        // then
        assertThatThrownBy(() ->
                attendanceService.validateGps(store, lat, lng, fetchedAt))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.GPS_OUT_OF_RANGE.getMessage());
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
        when(schedule.getEndTime()).thenReturn(LocalDateTime.now().minusMinutes(10)); // 이미 종료됨

        when(attendanceRepository.existsByScheduleId(1L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> attendanceService.recordClockIn(schedule))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.SCHEDULE_ALREADY_ENDED.getMessage());
    }

    @Test
    void 출근시간_이전일_경우_스케줄_출근시간으로_기록됨() {
        // given
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        LocalDateTime startTime = LocalDateTime.now().plusMinutes(5);
        when(schedule.getId()).thenReturn(2L);
        when(schedule.getStartTime()).thenReturn(startTime);
        when(schedule.getEndTime()).thenReturn(startTime.plusHours(4));

        when(attendanceRepository.existsByScheduleId(2L)).thenReturn(false);

        // when
        attendanceService.recordClockIn(schedule);

        // then
        verify(attendanceRepository).save(argThat(attendance ->
                attendance.getClockInTime().equals(startTime) &&
                        attendance.getClockInStatus().equals(ClockInStatus.NORMAL)
        ));
    }
}