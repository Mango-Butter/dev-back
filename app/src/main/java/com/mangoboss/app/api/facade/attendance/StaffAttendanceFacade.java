package com.mangoboss.app.api.facade.attendance;

import com.mangoboss.app.domain.service.attendance.context.AttendanceStrategyContext;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import com.mangoboss.app.dto.calender.WorkResponse;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import org.springframework.stereotype.Service;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.storage.store.StoreEntity;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StaffAttendanceFacade {
    private final AttendanceStrategyContext attendanceStrategyContext;

    private final StaffService staffService;
    private final ScheduleService scheduleService;
    private final AttendanceService attendanceService;

    public void clockIn(final Long storeId, final Long userId, final AttendanceBaseRequest request) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final StoreEntity store = staff.getStore();
        attendanceStrategyContext.validate(store, request);

        attendanceService.recordClockIn(staff.getId(), request.scheduleId());
    }

    public void clockOut(final Long storeId, final Long userId, final AttendanceBaseRequest request) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final StoreEntity store = staff.getStore();
        attendanceStrategyContext.validate(store, request);

        attendanceService.recordClockOut(staff.getId(), request.scheduleId());
    }

    public List<WorkResponse> getTodayWorks(final Long storeId, final Long userId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final List<ScheduleEntity> schedules = scheduleService.getSchedulesByStaffIdAndDate(staff.getId(), LocalDate.now());
        return schedules.stream().map(WorkResponse::of).toList();
    }
}
