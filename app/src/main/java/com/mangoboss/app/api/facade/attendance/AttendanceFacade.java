package com.mangoboss.app.api.facade.attendance;

import com.mangoboss.app.domain.service.attendance.strategy.clock_in.ClockInStrategy;
import com.mangoboss.app.domain.service.attendance.context.ClockInStrategyContext;
import com.mangoboss.app.domain.service.attendance.strategy.clock_out.ClockOutStrategy;
import com.mangoboss.app.domain.service.attendance.context.ClockOutStrategyContext;
import com.mangoboss.app.dto.attendance.base.ClockInBaseRequest;
import com.mangoboss.app.dto.attendance.base.ClockOutBaseRequest;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import org.springframework.stereotype.Service;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceFacade {
	private final ClockInStrategyContext clockInStrategyContext;
	private final ClockOutStrategyContext clockOutStrategyContext;

	private final StaffService staffService;
	private final StoreService storeService;
	private final ScheduleService scheduleService;
	private final AttendanceService attendanceService;
	private final UserService userService;

	public void clockIn(final Long userId, final Long storeId, final ClockInBaseRequest request) {
		// === 1. 매장 정보 조회 및 출퇴근 방식 일치 여부 검증 ===
		final StoreEntity store = storeService.getStoreById(storeId);
		attendanceService.validateMethodConsistency(store, request.attendanceMethod());

		// === 2. 전략 실행 (QR, GPS, BOTH 인증 검증) ===
		final ClockInStrategy<ClockInBaseRequest> strategy = clockInStrategyContext.resolve(store, request);
		strategy.validate(store, request);

		// === 3. 사용자, 알바생, 스케줄 검증 ===
		final UserEntity user = userService.getUserById(userId);
		final StaffEntity staff = staffService.getByUserAndStore(user, store);
		final ScheduleEntity schedule = scheduleService.getScheduleById(request.scheduleId());
		scheduleService.validateScheduleBelongsToStaff(schedule, staff);

		// === 4. 출근 기록 생성 ===
		attendanceService.recordClockIn(schedule);
	}

	public void clockOut(final Long userId, final Long storeId, final ClockOutBaseRequest request) {
		// === 1. 매장 정보 조회 및 출퇴근 방식 일치 여부 검증 ===
		final StoreEntity store = storeService.getStoreById(storeId);
		attendanceService.validateMethodConsistency(store, request.attendanceMethod());

		// === 2. 전략 실행 (QR, GPS, BOTH 인증 검증) ===
		final ClockOutStrategy<ClockOutBaseRequest> strategy = clockOutStrategyContext.resolve(store, request);
		strategy.validate(store, request);

		// === 3. 사용자, 알바생, 근태 기록 검증 ===
		final UserEntity user = userService.getUserById(userId);
		final StaffEntity staff = staffService.getByUserAndStore(user, store);
		final AttendanceEntity attendance = attendanceService.getAttendanceById(request.attendanceId()); // 새로운 메서드
		attendanceService.validateAttendanceBelongsToStaff(attendance, staff);

		// === 4. 퇴근 기록 업데이트 ===
		attendanceService.recordClockOut(attendance);
	}
}
