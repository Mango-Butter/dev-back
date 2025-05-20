package com.mangoboss.app.api.facade.attendance;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.attendance.request.AttendanceManualAddRequest;
import com.mangoboss.app.dto.attendance.request.AttendanceUpdateRequest;
import com.mangoboss.app.dto.attendance.response.AttendanceDetailResponse;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BossAttendanceFacade {
    private final StoreService storeService;
    private final StaffService staffService;
    private final AttendanceService attendanceService;
    private final ScheduleService scheduleService;

    public AttendanceDetailResponse getAttendanceDetail(final Long storeId, final Long bossId, final Long scheduleId) {
        storeService.isBossOfStore(storeId, bossId);
        final AttendanceEntity attendance = attendanceService.getScheduleWithAttendance(scheduleId);
        return AttendanceDetailResponse.fromEntity(attendance);
    }

    public AttendanceDetailResponse addManualAttendance(final Long storeId, final Long bossId, final AttendanceManualAddRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        attendanceService.validateWorkDateForManualAttendance(request.workDate());
        scheduleService.validateTime(request.clockInTime(), request.clockOutTime());

        final StaffEntity staff = staffService.validateStaffBelongsToStore(storeId, request.staffId());
        final AttendanceEntity attendance = attendanceService.createManualAttendanceAndSchedule(request.toSchedule(staff));
        return AttendanceDetailResponse.fromEntity(attendance);
    }

    public AttendanceDetailResponse updateAttendance(final Long storeId, final Long bossId, final Long scheduleId, final AttendanceUpdateRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        if(!request.clockInStatus().equals(ClockInStatus.ABSENT)) {
            scheduleService.validateTime(request.clockInTime(), request.clockOutTime());
        }

        final ScheduleEntity schedule = scheduleService.getScheduleById(scheduleId);
        final AttendanceEntity attendance = attendanceService.updateAttendance(
                schedule, request.toClockInDateTime(schedule.getWorkDate()), request.toClockOutDateTime(schedule.getWorkDate()), request.clockInStatus());
        return AttendanceDetailResponse.fromEntity(attendance);
    }

    public void deleteAttendance(final Long storeId, final Long bossId, final Long scheduleId) {
        storeService.isBossOfStore(storeId, bossId);
        attendanceService.deleteAttendanceWithSchedule(scheduleId);
    }

    public List<AttendanceDetailResponse> getAttendancesByStaffAndDateRange(final Long storeId, final Long staffId, final Long bossId,
                                                                            final LocalDate start, final LocalDate end) {
        storeService.isBossOfStore(storeId, bossId);
        staffService.validateStaffBelongsToStore(storeId, staffId);
        final List<AttendanceEntity> attendances = attendanceService.getAttendancesByStaffAndDateRange(staffId, start, end);
        return attendances.stream().map(AttendanceDetailResponse::fromEntity).toList();
    }
}
