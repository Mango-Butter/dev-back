package com.mangoboss.app.domain.service.attendance;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private static final long OVERTIME_THRESHOLD_MINUTES = 10;  //todo 설정 이후에는 삭제해야 함.
    private static final long CLOCK_IN_ALLOWED_MINUTES_BEFORE = 10;

    public AttendanceEntity recordClockIn(final Long staffId, final Long scheduleId) {
        final ScheduleEntity schedule = scheduleRepository.getByIdAndStaffId(scheduleId, staffId);
        final LocalDateTime now = LocalDateTime.now(clock).withSecond(0).withNano(0);

        validateNotAlreadyClockedIn(schedule);
        validateScheduleNotEnded(schedule.getEndTime(), now);
        final LocalDateTime clockInTime = verifyClockInTime(schedule.getStartTime(), now);
        final ClockInStatus clockInStatus = determineClockInStatus(schedule.getStartTime(), clockInTime);

        final AttendanceEntity attendance = AttendanceEntity.create(schedule, clockInTime, clockInStatus);
        return attendanceRepository.save(attendance);
    }

    public void recordClockOut(final Long staffId, final Long scheduleId) {
        final ScheduleEntity schedule = scheduleRepository.getByIdAndStaffId(scheduleId, staffId);
        final LocalDateTime now = LocalDateTime.now(clock).withSecond(0).withNano(0);
        validateClockedIn(schedule);
        final AttendanceEntity attendance = schedule.getAttendance();

        validateNotAlreadyClockedOut(attendance);
        final ClockOutStatus clockOutStatus = determineClockOutStatus(schedule.getEndTime(), now);

        attendance.recordClockOut(now, clockOutStatus);
    }

    private void validateClockedIn(final ScheduleEntity schedule) {
        if (schedule.getAttendance() == null) {
            throw new CustomException(CustomErrorInfo.NOT_CLOCKED_IN_YET);
        }
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

    private ClockInStatus determineClockInStatus(final LocalDateTime scheduledStartTime, final LocalDateTime clockInTime) {
        return clockInTime.isBefore(scheduledStartTime) ? ClockInStatus.NORMAL : ClockInStatus.LATE;
    }

    private ClockOutStatus determineClockOutStatus(final LocalDateTime scheduledEndTime, final LocalDateTime clockOutTime) {
        if (clockOutTime.isBefore(scheduledEndTime)) {
            return ClockOutStatus.EARLY_LEAVE;
        }

        final long overtimeMinutes = Duration.between(scheduledEndTime, clockOutTime).toMinutes();
        if (overtimeMinutes >= OVERTIME_THRESHOLD_MINUTES) {
            return ClockOutStatus.OVERTIME;
        }

        return ClockOutStatus.NORMAL;
    }

    private LocalDateTime verifyClockInTime(final LocalDateTime scheduleStartTime, final LocalDateTime now) {
        if (now.isBefore(scheduleStartTime.minusMinutes(CLOCK_IN_ALLOWED_MINUTES_BEFORE))) {
            throw new CustomException(CustomErrorInfo.EARLY_CLOCK_IN);
        }
        return now.isBefore(scheduleStartTime) ? scheduleStartTime : now;
    }

    @Transactional(readOnly = true)
    public ScheduleEntity getScheduleWithAttendance(final Long scheduleId) {
        return scheduleRepository.getByIdAndAttendanceIsNotNull(scheduleId);
    }

    @Transactional(readOnly = true)
    public List<WorkDotProjection> getWorkDots(final Long storeId, final LocalDate start, final LocalDate end) {
        return attendanceRepository.findWorkDotProjections(storeId, start, end);
    }
}

