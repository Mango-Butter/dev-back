package com.mangoboss.app.api.facade.attendance;

import com.mangoboss.app.domain.service.attendance.AttendanceEditService;
import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.notification.NotificationService;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.attendance.request.AttendanceManualAddRequest;
import com.mangoboss.app.dto.attendance.request.AttendanceUpdateRequest;
import com.mangoboss.app.dto.attendance.response.AttendanceDetailResponse;
import com.mangoboss.app.dto.attendance.response.AttendanceEditResponse;
import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BossAttendanceFacade {
    private final StoreService storeService;
    private final StaffService staffService;
    private final AttendanceService attendanceService;
    private final AttendanceEditService attendanceEditService;
    private final ScheduleService scheduleService;
    private final NotificationService notificationService;

    public AttendanceDetailResponse getAttendanceDetail(final Long storeId, final Long bossId, final Long scheduleId) {
        storeService.isBossOfStore(storeId, bossId);
        AttendanceEntity attendance = attendanceService.getScheduleWithAttendance(scheduleId);
        return AttendanceDetailResponse.fromEntity(attendance);
    }

    public AttendanceDetailResponse addManualAttendance(final Long storeId, final Long bossId, final AttendanceManualAddRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        attendanceService.validateWorkDateForManualAttendance(request.workDate());
        scheduleService.validateTime(request.clockInTime(), request.clockOutTime());

        StaffEntity staff = staffService.validateStaffBelongsToStore(storeId, request.staffId());
        AttendanceEntity attendance = attendanceService.createManualAttendanceAndSchedule(request.toSchedule(staff));
        return AttendanceDetailResponse.fromEntity(attendance);
    }

    public AttendanceDetailResponse updateAttendance(final Long storeId, final Long bossId, final Long scheduleId, final AttendanceUpdateRequest request) {
        StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        if (!request.clockInStatus().equals(ClockInStatus.ABSENT)) {
            scheduleService.validateTime(request.clockInTime(), request.clockOutTime());
        }

        ScheduleEntity schedule = scheduleService.getScheduleById(scheduleId);
        AttendanceEntity attendance = attendanceService.updateAttendance(
                schedule,
                store.getOvertimeLimit(),
                request.toClockInDateTime(schedule.getWorkDate()),
                request.toClockOutDateTime(schedule.getWorkDate()),
                request.clockInStatus());
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
        List<AttendanceEntity> attendances = attendanceService.getAttendancesByStaffAndDateRange(staffId, start, end);
        return attendances.stream().map(AttendanceDetailResponse::fromEntity).toList();
    }

    public List<AttendanceEditResponse> getAttendanceEdits(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        List<AttendanceEditEntity> attendanceEdits = attendanceEditService.getAttendanceByStoreId(storeId);
        return attendanceEdits.stream()
                .map(AttendanceEditResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void approveAttendanceEdit(final Long storeId, final Long editId, final Long bossId) {
        StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        AttendanceEditEntity attendanceEdit = attendanceEditService.approveAttendanceEdit(editId);
        notificationService.saveAttendanceEditApproveNotification(attendanceEdit);
    }

    @Transactional
    public void rejectAttendanceEdit(final Long storeId, final Long editId, final Long bossId) {
        StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        AttendanceEditEntity attendanceEdit = attendanceEditService.rejectAttendanceEdit(editId);
        notificationService.saveAttendanceEditRejectNotification(attendanceEdit);
    }
}
