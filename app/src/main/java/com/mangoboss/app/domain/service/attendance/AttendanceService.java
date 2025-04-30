package com.mangoboss.app.domain.service.attendance;

import java.time.Duration;
import java.time.LocalDateTime;

import com.mangoboss.app.domain.repository.AttendanceRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.attendance.ClockOutStatus;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.AttendanceMethod;
import org.springframework.stereotype.Service;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.storage.store.StoreEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;

	private static final long MAX_GPS_TIME_DIFF_SECONDS = 10; // GPS 데이터 유효 시간 (초)
	private static final long OVERTIME_THRESHOLD_MINUTES = 10; // 예: 10분 초과 근무 시 연장 근무로 판단

	public void validateQr(final StoreEntity store, final String qrCode) {
		if (!store.getQrCode().equals(qrCode)) {
			throw new CustomException(CustomErrorInfo.INVALID_QR_CODE);
		}
	}

	public void validateGps(final StoreEntity store, final Double latitude, final Double longitude, final LocalDateTime fetchedAt) {
		if (Duration.between(fetchedAt, LocalDateTime.now()).abs().getSeconds() > MAX_GPS_TIME_DIFF_SECONDS) {
			throw new CustomException(CustomErrorInfo.INVALID_GPS_TIME);
		}

		final double distance = calculateDistance(store.getGpsLatitude(), store.getGpsLongitude(), latitude, longitude);

		if (distance > store.getGpsRangeMeters()) {
			throw new CustomException(CustomErrorInfo.GPS_OUT_OF_RANGE);
		}
	}

	// 출근 기록
	public void recordClockIn(final ScheduleEntity schedule) {
		if (attendanceRepository.existsByScheduleId(schedule.getId())) {
			throw new CustomException(CustomErrorInfo.ALREADY_CLOCKED_IN);
		}

		final LocalDateTime now = LocalDateTime.now();
		final LocalDateTime clockInTime = getClockInTime(schedule.getStartTime(), now);
		final ClockInStatus clockInStatus = getClockInStatus(schedule.getStartTime(), clockInTime);

		final AttendanceEntity attendance = AttendanceEntity.create(schedule, clockInTime, clockInStatus);
		attendanceRepository.save(attendance);
	}

	// 퇴근 기록
	public void recordClockOut(final AttendanceEntity attendance) {
		if (attendance.isAlreadyClockedOut()) {
			throw new CustomException(CustomErrorInfo.ALREADY_CLOCKED_OUT);
		}

		final LocalDateTime now = LocalDateTime.now();
		final LocalDateTime scheduledEndTime = attendance.getSchedule().getEndTime();
		final ClockOutStatus clockOutStatus = getClockOutStatus(scheduledEndTime, now);

		attendance.recordClockOut(now, clockOutStatus);
	}

	private LocalDateTime getClockInTime(final LocalDateTime startTime, final LocalDateTime now) {
		if (now.isBefore(startTime)) { // 출근 시간 전에 출근시, 근태에는 스케줄의 출근 시간으로 기록
			return startTime;
		}
		return now; // 출근 시간 이후에 출근시, 근태에는 출근 클릭 시간으로 기록
	}

	// 출근 상태 판단
	private ClockInStatus getClockInStatus(final LocalDateTime scheduledStart, final LocalDateTime actualTime) {
		if (actualTime.isAfter(scheduledStart)) {
			return ClockInStatus.LATE;
		}
		return ClockInStatus.NORMAL;
	}

	// 퇴근 상태 판단
	private ClockOutStatus getClockOutStatus(final LocalDateTime scheduledEndTime, final LocalDateTime clockOutTime) {
		if (clockOutTime.isBefore(scheduledEndTime)) {
			return ClockOutStatus.EARLY_LEAVE;
		}

		final long overtimeMinutes = Duration.between(scheduledEndTime, clockOutTime).toMinutes();
		if (overtimeMinutes >= OVERTIME_THRESHOLD_MINUTES) {
			return ClockOutStatus.OVERTIME;
		}

		return ClockOutStatus.NORMAL;
	}

	public void validateMethodConsistency(final StoreEntity store, final String requestMethod) {
		final AttendanceMethod request;
		try {
			request = AttendanceMethod.valueOf(requestMethod.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new CustomException(CustomErrorInfo.INVALID_ATTENDANCE_METHOD);
		}

		final AttendanceMethod current = store.getAttendanceMethod();

		if (current != request) {
			throw new CustomException(CustomErrorInfo.ATTENDANCE_METHOD_CHANGED);
		}
	}

	@Transactional(readOnly = true)
	public AttendanceEntity getAttendanceById(final Long attendanceId) {
		return attendanceRepository.getById(attendanceId);
	}

	@Transactional(readOnly = true)
	public void validateAttendanceBelongsToStaff(final AttendanceEntity attendance, final StaffEntity staff) {
		if (!attendance.getSchedule().getStaff().equals(staff)) {
			throw new CustomException(CustomErrorInfo.ATTENDANCE_NOT_BELONG_TO_STAFF);
		}
	}

	// 거리 계산
	private Double calculateDistance(final Double lat1, final Double lon1, final Double lat2, final Double lon2) {
		final double R = 6371000.0; // earth radius in meters
		final double dLat = Math.toRadians(lat2 - lat1);
		final double dLon = Math.toRadians(lon2 - lon1);
		final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}

}

