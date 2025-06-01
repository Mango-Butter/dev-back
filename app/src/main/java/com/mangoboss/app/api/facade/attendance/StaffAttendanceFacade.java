package com.mangoboss.app.api.facade.attendance;

import com.mangoboss.app.domain.service.attendance.AttendanceEditService;
import com.mangoboss.app.domain.service.attendance.context.AttendanceStrategyContext;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.dto.attendance.response.AttendanceEditResponse;
import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import com.mangoboss.app.dto.attendance.request.AttendanceEditRequest;
import com.mangoboss.app.dto.attendance.response.AttendanceDetailResponse;
import com.mangoboss.app.dto.calender.WorkResponse;
import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.attendance.AttendanceEntity;
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
    private final AttendanceEditService attendanceEditRequestService;

    public void clockIn(final Long storeId, final Long userId, final AttendanceBaseRequest request) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        StoreEntity store = staff.getStore();
        attendanceStrategyContext.validate(store, request);

        attendanceService.recordClockIn(staff.getId(), request.scheduleId());
    }

    public void clockOut(final Long storeId, final Long userId, final AttendanceBaseRequest request) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        StoreEntity store = staff.getStore();
        attendanceStrategyContext.validate(store, request);

        attendanceService.recordClockOut(staff.getId(), request.scheduleId(), store.getOvertimeLimit());
    }

    public List<WorkResponse> getTodayWorks(final Long storeId, final Long userId) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        List<ScheduleEntity> schedules = scheduleService.getSchedulesByStaffIdAndDate(staff.getId(), LocalDate.now());
        return schedules.stream().map(WorkResponse::of).toList();
    }

    public List<AttendanceDetailResponse> getAttendancesByDateRange(final Long storeId, final Long userId,
                                                                    final LocalDate start, final LocalDate end) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        List<AttendanceEntity> attendances = attendanceService.getAttendancesByStaffAndDateRange(staff.getId(), start, end);
        return attendances.stream().map(AttendanceDetailResponse::fromEntity).toList();
    }

    public AttendanceDetailResponse getAttendanceDetail(final Long storeId, final Long userId, final Long scheduleId) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        ScheduleEntity schedule = scheduleService.validateScheduleBelongsToStaff(scheduleId, staff.getId());
        return AttendanceDetailResponse.fromEntity(schedule.getAttendance());
    }

    public void requestAttendanceEdit(final Long storeId, final Long userId, final Long scheduleId, final AttendanceEditRequest request) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        ScheduleEntity schedule = scheduleService.validateScheduleBelongsToStaff(scheduleId, staff.getId());
        AttendanceEntity attendance = schedule.getAttendance();
        attendanceEditRequestService.requestAttendanceEdit(attendance, request.toEntity(attendance));
    }

    public List<AttendanceEditResponse> getAttendanceEdits(final Long storeId, final Long userId) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        List<AttendanceEditEntity> attendanceEdits = attendanceEditRequestService.getAttendanceEditsByStaff(staff.getId());
        return attendanceEdits.stream()
                .map(AttendanceEditResponse::fromEntity)
                .toList();
    }
}
