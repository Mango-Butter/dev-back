package com.mangoboss.app.domain.service.attendance;

import java.time.*;
import java.util.List;

import com.mangoboss.app.domain.repository.AttendanceRepository;
import com.mangoboss.app.domain.repository.ScheduleRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.attendance.ClockOutStatus;
import com.mangoboss.storage.attendance.projection.WorkDotProjection;
import com.mangoboss.storage.schedule.ScheduleEntity;
import org.springframework.stereotype.Service;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;
    private final Clock clock;

    private static final long OVERTIME_THRESHOLD_MINUTES = 30;  //todo 설정 이후에는 삭제해야 함.
    private static final long CLOCK_ALLOWED_MINUTES = 10;

    public AttendanceEntity recordClockIn(final Long staffId, final Long scheduleId) {
        final ScheduleEntity schedule = scheduleRepository.getByIdAndStaffId(scheduleId, staffId);
        final LocalDateTime now = LocalDateTime.now(clock).withSecond(0).withNano(0);

        validateNotAlreadyClockedIn(schedule);
        validateScheduleNotEnded(schedule.getEndTime(), now);
        final LocalDateTime clockInTime = verifyClockInTime(schedule.getStartTime(), now);
        final ClockInStatus clockInStatus = determineClockInStatus(schedule.getStartTime(), clockInTime);

        final AttendanceEntity attendance = AttendanceEntity.createForClockIn(schedule, clockInTime, clockInStatus);
        return attendanceRepository.save(attendance);
    }

    public void recordClockOut(final Long staffId, final Long scheduleId) {
        final ScheduleEntity schedule = scheduleRepository.getByIdAndStaffId(scheduleId, staffId);
        final LocalDateTime now = LocalDateTime.now(clock).withSecond(0).withNano(0);

        final AttendanceEntity attendance = validateClockedIn(schedule);
        validateNotAlreadyClockedOut(attendance);
        final LocalDateTime clockOutTime = verifyClockOutTime(schedule.getEndTime(), now);
        final ClockOutStatus clockOutStatus = determineClockOutStatus(schedule.getEndTime(), now);

        attendance.recordClockOut(clockOutTime, clockOutStatus);
    }

    private AttendanceEntity validateClockedIn(final ScheduleEntity schedule) {
        if (schedule.getAttendance() == null) {
            throw new CustomException(CustomErrorInfo.NOT_CLOCKED_IN_YET);
        }
        return schedule.getAttendance();
    }

    private void validateNotAlreadyClockedIn(final ScheduleEntity schedule) {
        if (schedule.getAttendance() != null) {
            throw new CustomException(CustomErrorInfo.ALREADY_CLOCKED_IN);
        }
    }

    private void validateScheduleNotEnded(final LocalDateTime endTime, final LocalDateTime now) {
        if (now.isAfter(endTime)) {
            throw new CustomException(CustomErrorInfo.SCHEDULE_ALREADY_ENDED);
        }
    }

    private void validateNotAlreadyClockedOut(final AttendanceEntity attendance) {
        if (attendance.isAlreadyClockedOut()) {
            throw new CustomException(CustomErrorInfo.ALREADY_CLOCKED_OUT);
        }
    }

    private void validateAlreadyClockedOut(final AttendanceEntity attendance) {
        if (!attendance.isAlreadyClockedOut()) {
            throw new CustomException(CustomErrorInfo.INCOMPLETE_ATTENDANCE);
        }
    }

    private ClockInStatus determineClockInStatus(final LocalDateTime scheduledStartTime, final LocalDateTime clockInTime) {
        return clockInTime.isBefore(scheduledStartTime) ? ClockInStatus.NORMAL : ClockInStatus.LATE;
    }

    private ClockOutStatus determineClockOutStatus(final LocalDateTime scheduledEndTime, final LocalDateTime clockOutTime) {
        final LocalDateTime limitTime = scheduledEndTime.plusMinutes(CLOCK_ALLOWED_MINUTES);
        if (clockOutTime.isBefore(scheduledEndTime)) {
            return ClockOutStatus.EARLY_LEAVE;
        }
        if (clockOutTime.isAfter(limitTime)) {  // todo 매장이 초과근무 허용할때만
            return ClockOutStatus.OVERTIME;
        }

        return ClockOutStatus.NORMAL;
    }

    private LocalDateTime verifyClockInTime(final LocalDateTime scheduleStartTime, final LocalDateTime now) {
        if (now.isBefore(scheduleStartTime.minusMinutes(CLOCK_ALLOWED_MINUTES))) {
            throw new CustomException(CustomErrorInfo.EARLY_CLOCK_IN);
        }
        return now.isBefore(scheduleStartTime) ? scheduleStartTime : now;
    }

    private LocalDateTime verifyClockOutTime(final LocalDateTime scheduleEndTime, final LocalDateTime now) {
        if (now.isBefore(scheduleEndTime)) {
            return now;
        }
        if (now.isBefore(scheduleEndTime.plusMinutes(CLOCK_ALLOWED_MINUTES))) {
            return scheduleEndTime;
        }
        if (now.isBefore(scheduleEndTime.plusMinutes(OVERTIME_THRESHOLD_MINUTES))) {
            return now;
        }
        return scheduleEndTime.plusMinutes(OVERTIME_THRESHOLD_MINUTES);
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

    public AttendanceEntity createManualAttendance(final ScheduleEntity schedule) {
        scheduleRepository.save(schedule);
        final AttendanceEntity attendance = AttendanceEntity.create(
                schedule.getStartTime(), schedule.getEndTime(), ClockInStatus.NORMAL, ClockOutStatus.NORMAL, schedule);
        return attendanceRepository.save(attendance);
    }

    public AttendanceEntity updateAttendance(final ScheduleEntity schedule, final LocalDateTime clockInTime, final LocalDateTime clockOutTime, final ClockInStatus clockInStatus) {
        final AttendanceEntity attendance = attendanceRepository.getByScheduleId(schedule.getId());
        validateAlreadyClockedOut(attendance);
        if (clockInStatus.equals(ClockInStatus.ABSENT)) {
            return attendance.update(null, null, clockInStatus, null);
        }
        final ClockOutStatus clockOutStatus = determineClockOutStatus(schedule.getEndTime(), clockOutTime);
        return attendance.update(clockInTime, clockOutTime, clockInStatus, clockOutStatus);
    }
}

