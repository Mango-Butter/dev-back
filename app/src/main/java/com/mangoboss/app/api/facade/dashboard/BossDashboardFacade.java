package com.mangoboss.app.api.facade.dashboard;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.dashboard.response.StaffAttendanceSummaryResponse;
import com.mangoboss.storage.attendance.projection.StaffAttendanceCountProjection;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BossDashboardFacade {

    private final StoreService storeService;
    private final ScheduleService scheduleService;
    private final AttendanceService attendanceService;

    public List<StaffAttendanceSummaryResponse> getStaffAttendanceDashboard(final Long storeId, final Long bossId, final LocalDate startDate, final LocalDate endDate) {
        storeService.isBossOfStore(storeId, bossId);
        final List<StaffAttendanceCountProjection> staffAttendanceCountProjection = attendanceService.getAttendanceCountsByStoreId(storeId, startDate, endDate);
        return staffAttendanceCountProjection
                .stream()
                .map(projection -> {
                    StaffEntity staff = projection.getStaff();
                    return StaffAttendanceSummaryResponse.of(
                            staff,
                            scheduleService.getDayOfWeeksForRegularGroup(staff.getId()),
                            projection);
                })
                .toList();
    }
}