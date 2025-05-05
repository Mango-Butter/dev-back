package com.mangoboss.app.api.facade.attendance;

import com.mangoboss.app.domain.service.attendance.context.AttendanceStrategyContext;
import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import com.mangoboss.storage.staff.StaffEntity;
import org.springframework.stereotype.Service;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.storage.store.StoreEntity;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class StaffAttendanceFacade {
	private final AttendanceStrategyContext attendanceStrategyContext;

	private final StaffService staffService;
	private final AttendanceService attendanceService;

	public void clockIn(final Long userId, final Long storeId, final AttendanceBaseRequest request) {
		final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
		final StoreEntity store = staff.getStore();
		attendanceStrategyContext.validate(store, request);

		attendanceService.recordClockIn(staff.getId(), request.scheduleId());
	}

	public void clockOut(final Long userId, final Long storeId, final AttendanceBaseRequest request) {
		final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
		final StoreEntity store = staff.getStore();
		attendanceStrategyContext.validate(store, request);

		attendanceService.recordClockOut(staff.getId(), request.scheduleId());
	}
}
