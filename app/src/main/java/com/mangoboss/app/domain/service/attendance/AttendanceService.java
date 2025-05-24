package com.mangoboss.app.domain.service.attendance;

import java.time.*;
import java.util.List;
import java.util.Map;

import com.mangoboss.app.domain.repository.AttendanceRepository;
import com.mangoboss.app.domain.repository.ScheduleRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.attendance.ClockOutStatus;
import com.mangoboss.storage.attendance.projection.WorkDotProjection;
import com.mangoboss.storage.attendance.projection.StaffAttendanceCountProjection;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.store.StoreEntity;
import org.springframework.stereotype.Service;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private static final long CLOCK_ALLOWED_MINUTES = 10;

    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;
    private final Clock clock;

    private record ClockOutResult(LocalDateTime clockOutTime, ClockOutStatus status) {
    }

    private record ClockInResult(LocalDateTime clockInTime, ClockInStatus status) {
    }

    public AttendanceEntity recordClockIn(final Long staffId, final Long scheduleId) {
        final ScheduleEntity schedule = scheduleRepository.getByIdAndStaffId(scheduleId, staffId);
        final LocalDateTime now = LocalDateTime.now(clock).withSecond(0).withNano(0);

        validateNotClockedIn(schedule);
        validateScheduleNotEnded(schedule.getEndTime(), now);
        final ClockInResult clockInResult = verifyClockInTime(schedule.getStartTime(), now);
        final AttendanceEntity attendance = AttendanceEntity.createForClockIn(
                schedule, clockInResult.clockInTime, clockInResult.status
        );
        return attendanceRepository.save(attendance);
    }

    public void recordClockOut(final Long staffId, final Long scheduleId, final Integer overtimeLimit) {
        final ScheduleEntity schedule = scheduleRepository.getByIdAndStaffId(scheduleId, staffId);
        final LocalDateTime now = LocalDateTime.now(clock).withSecond(0).withNano(0);

        final AttendanceEntity attendance = validateClockedIn(schedule);
        validateNotClockedOut(attendance);
        final ClockOutResult clockOutResult = verifyClockOutTime(schedule.getEndTime(), now, overtimeLimit);

        attendance.recordClockOut(clockOutResult.clockOutTime, clockOutResult.status);
    }

    private AttendanceEntity validateClockedIn(final ScheduleEntity schedule) {
        if (schedule.getAttendance() == null) {
            throw new CustomException(CustomErrorInfo.NOT_CLOCKED_IN_YET);
        }
        return schedule.getAttendance();
    }

    private void validateNotClockedIn(final ScheduleEntity schedule) {
        if (schedule.getAttendance() != null) {
            throw new CustomException(CustomErrorInfo.ALREADY_CLOCKED_IN);
        }
    }

    private void validateScheduleNotEnded(final LocalDateTime endTime, final LocalDateTime now) {
        if (now.isAfter(endTime)) {
            throw new CustomException(CustomErrorInfo.SCHEDULE_ALREADY_ENDED);
        }
    }

    private void validateNotClockedOut(final AttendanceEntity attendance) {
        if (attendance.isAlreadyClockedOut()) {
            throw new CustomException(CustomErrorInfo.ALREADY_CLOCKED_OUT);
        }
    }

    private void validateClockedOut(final AttendanceEntity attendance) {
        if (!attendance.isAlreadyClockedOut()) {
            throw new CustomException(CustomErrorInfo.INCOMPLETE_ATTENDANCE);
        }
    }

    private ClockOutStatus determineClockOutStatus(final LocalDateTime scheduledEndTime, final LocalDateTime clockOutTime, final Integer overtimeLimit) {
        final LocalDateTime limitTime = scheduledEndTime.plusMinutes(overtimeLimit);
        if (clockOutTime.isBefore(scheduledEndTime)) {
            return ClockOutStatus.EARLY_LEAVE;
        }
        if (clockOutTime.isAfter(limitTime)) {
            return ClockOutStatus.OVERTIME;
        }

        return ClockOutStatus.NORMAL;
    }

    private ClockInResult verifyClockInTime(final LocalDateTime scheduleStartTime, final LocalDateTime now) {
        if (now.isBefore(scheduleStartTime.minusMinutes(CLOCK_ALLOWED_MINUTES))) {
            throw new CustomException(CustomErrorInfo.EARLY_CLOCK_IN);
        }
        return now.isBefore(scheduleStartTime) ? new ClockInResult(scheduleStartTime, ClockInStatus.NORMAL)
                : new ClockInResult(now, ClockInStatus.LATE);
    }

    private ClockOutResult verifyClockOutTime(final LocalDateTime scheduleEndTime, final LocalDateTime now, final Integer overtimeLimit) {
        if (now.isBefore(scheduleEndTime)) {
            return new ClockOutResult(now, ClockOutStatus.EARLY_LEAVE);
        }
        if (now.isBefore(scheduleEndTime.plusMinutes(CLOCK_ALLOWED_MINUTES))) {
            return new ClockOutResult(now, ClockOutStatus.NORMAL);
        }
        if (now.isBefore(scheduleEndTime.plusMinutes(overtimeLimit))) {
            return new ClockOutResult(now, ClockOutStatus.OVERTIME);
        }
        return new ClockOutResult(scheduleEndTime, ClockOutStatus.NORMAL);
    }

    @Transactional(readOnly = true)
    public AttendanceEntity getScheduleWithAttendance(final Long scheduleId) {
        return attendanceRepository.getByScheduleId(scheduleId);
    }

    @Transactional(readOnly = true)
    public List<WorkDotProjection> getWorkDots(final Long storeId, final LocalDate start, final LocalDate end) {
        return attendanceRepository.findWorkDotProjections(storeId, start, end);
    }

    @Transactional(readOnly = true)
    public void validateWorkDateForManualAttendance(final LocalDate workDate) {
        final LocalDate now = LocalDate.now(clock);
        if (!workDate.isBefore(now)) {
            throw new CustomException(CustomErrorInfo.ATTENDANCE_DATE_MUST_BE_PAST);
        }
    }

    public AttendanceEntity createManualAttendanceAndSchedule(final ScheduleEntity schedule) {
        scheduleRepository.save(schedule);
        final AttendanceEntity attendance = AttendanceEntity.create(
                schedule.getStartTime(), schedule.getEndTime(), ClockInStatus.NORMAL, ClockOutStatus.NORMAL, schedule);
        return attendanceRepository.save(attendance);
    }

    public AttendanceEntity updateAttendance(final ScheduleEntity schedule, final Integer overtimeLimit, final LocalDateTime clockInTime, final LocalDateTime clockOutTime, final ClockInStatus clockInStatus) {
        final AttendanceEntity attendance = attendanceRepository.getByScheduleId(schedule.getId());
        if (clockInStatus.equals(ClockInStatus.ABSENT)) {
            return attendance.update(null, null, ClockInStatus.ABSENT, ClockOutStatus.ABSENT);
        }
        validateClockedOut(attendance);
        final ClockOutStatus clockOutStatus = determineClockOutStatus(schedule.getEndTime(), clockOutTime, overtimeLimit);
        return attendance.update(clockInTime, clockOutTime, clockInStatus, clockOutStatus);
    }

    public void deleteAttendanceWithSchedule(final Long scheduleId) {
        final AttendanceEntity attendance = attendanceRepository.getByScheduleId(scheduleId);
        final ScheduleEntity schedule = attendance.getSchedule();
        validateClockedOut(attendance);
        attendanceRepository.delete(attendance);
        scheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    public List<StaffAttendanceCountProjection> getAttendanceCountsByStoreId(final Long storeId, final LocalDate start, final LocalDate end) {
        return attendanceRepository.findAttendanceCountsByStoreId(storeId, start, end);
    }

    @Transactional(readOnly = true)
    public List<AttendanceEntity> getAttendancesByStaffAndDateRange(final Long staffId, final LocalDate start, final LocalDate end) {
        return attendanceRepository.findByStaffIdAndWorkDateBetween(staffId, start, end);
    }
}

