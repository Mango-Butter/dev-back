package com.mangoboss.app.api.facade.attendance;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.attendance.request.AttendanceManualAddRequest;
import com.mangoboss.app.dto.attendance.response.AttendanceDetailResponse;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        scheduleService.validateTime(request.startTime(), request.endTime());

        final StaffEntity staff = staffService.getStaffBelongsToStore(storeId, request.staffId());
        final AttendanceEntity attendance = attendanceService.createManualAttendance(staff, request.workDate(), request.toStartDateTime(), request.toEndDateTime());
        return AttendanceDetailResponse.fromEntity(attendance);
    }
}
