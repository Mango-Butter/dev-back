package com.mangoboss.app.api.facade.attendance;

import com.mangoboss.app.domain.service.attendance.context.AttendanceStrategyContext;
import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import com.mangoboss.app.dto.attendance.response.ClockInResponse;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import org.springframework.stereotype.Service;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.storage.store.StoreEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceFacade {
	private final AttendanceStrategyContext attendanceStrategyContext;

	private final StaffService staffService;
	private final StoreService storeService;
	private final ScheduleService scheduleService;
	private final AttendanceService attendanceService;

	public ClockInResponse clockIn(final Long userId, final Long storeId, final AttendanceBaseRequest request) {
		final Long staffId = staffService.getVerifiedStaffId(userId, storeId);
		final ScheduleEntity schedule = scheduleService.getVerifiedScheduleByStaff(request.scheduleId(), staffId);

		final StoreEntity store = storeService.getStoreById(storeId);
		attendanceStrategyContext.validate(store, request);

		final AttendanceEntity attendance = attendanceService.recordClockIn(schedule);
		return ClockInResponse.fromEntity(attendance);
	}

	public void clockOut(final Long userId, final Long storeId, final AttendanceBaseRequest request) {
		final StaffEntity staff = staffService.getByUserIdAndStoreId(userId, storeId);
		final AttendanceEntity attendance = attendanceService.getAttendanceByScheduleId(request.scheduleId());
		attendanceService.validateAttendanceBelongsToStaff(attendance, staff);

		final StoreEntity store = storeService.getStoreById(storeId);
		attendanceStrategyContext.validate(store, request);

		attendanceService.recordClockOut(attendance);
	}
}
