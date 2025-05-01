package com.mangoboss.app.domain.service.attendance;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

import com.mangoboss.app.domain.repository.AttendanceRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.attendance.ClockOutStatus;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
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
	private final Clock clock;

	private static final long OVERTIME_THRESHOLD_MINUTES = 10;

	public AttendanceEntity recordClockIn(final ScheduleEntity schedule) {
		final LocalDateTime now = LocalDateTime.now(clock);
		validateNotAlreadyClockedIn(schedule.getId());
		validateScheduleNotEnded(schedule.getEndTime(), now);
		final LocalDateTime clockInTime = getClockInTime(schedule.getStartTime(), now);
		final ClockInStatus clockInStatus = getClockInStatus(schedule.getStartTime(), clockInTime);
		final AttendanceEntity attendance = AttendanceEntity.create(schedule, clockInTime, clockInStatus);
		return attendanceRepository.save(attendance);
	}

	public void recordClockOut(final AttendanceEntity attendance) {
		final LocalDateTime now = LocalDateTime.now(clock);
		validateNotAlreadyClockedOut(attendance);
		final LocalDateTime scheduledEndTime = attendance.getSchedule().getEndTime();
		final ClockOutStatus clockOutStatus = getClockOutStatus(scheduledEndTime, now);

		attendance.recordClockOut(now, clockOutStatus);
	}

	private void validateNotAlreadyClockedIn(final Long scheduleId) {
		if (attendanceRepository.existsByScheduleId(scheduleId)) {
			throw new CustomException(CustomErrorInfo.ALREADY_CLOCKED_IN);
		}
	}

	private void validateScheduleNotEnded(final LocalDateTime endTime, final LocalDateTime now) {
		if (!isBeforeTime(now, endTime)) {
			throw new CustomException(CustomErrorInfo.SCHEDULE_ALREADY_ENDED);
		}
	}

	private void validateNotAlreadyClockedOut(final AttendanceEntity attendance) {
		if (attendance.isAlreadyClockedOut()) {
			throw new CustomException(CustomErrorInfo.ALREADY_CLOCKED_OUT);
		}
	}

	private ClockInStatus getClockInStatus(final LocalDateTime scheduledStartTime, final LocalDateTime clockInTime) {
		return isBeforeTime(clockInTime, scheduledStartTime)
				? ClockInStatus.NORMAL
				: ClockInStatus.LATE;
	}

	private ClockOutStatus getClockOutStatus(final LocalDateTime scheduledEndTime, final LocalDateTime clockOutTime) {
		if (isBeforeTime(clockOutTime, scheduledEndTime)) {
			return ClockOutStatus.EARLY_LEAVE;
		}

		final long overtimeMinutes = Duration.between(scheduledEndTime, clockOutTime).toMinutes();
		if (overtimeMinutes >= OVERTIME_THRESHOLD_MINUTES) {
			return ClockOutStatus.OVERTIME;
		}

		return ClockOutStatus.NORMAL;
	}

	private LocalDateTime getClockInTime(final LocalDateTime scheduleStartTime, final LocalDateTime now) {
		return isBeforeTime(now, scheduleStartTime) ? scheduleStartTime : now;
	}

	private boolean isBeforeTime(final LocalDateTime checked, final LocalDateTime reference) {
        return checked.isBefore(reference);
	}

	@Transactional(readOnly = true)
	public AttendanceEntity getAttendanceById(final Long attendanceId) {
		return attendanceRepository.getById(attendanceId);
	}

	@Transactional(readOnly = true)
	public AttendanceEntity getAttendanceByScheduleId(final Long scheduleId) {
		return attendanceRepository.getByScheduleId(scheduleId);
	}

	@Transactional(readOnly = true)
	public void validateAttendanceBelongsToStaff(final AttendanceEntity attendance, final StaffEntity staff) {
		if (!attendance.getSchedule().getStaff().equals(staff)) {
			throw new CustomException(CustomErrorInfo.ATTENDANCE_NOT_BELONG_TO_STAFF);
		}
	}

}

