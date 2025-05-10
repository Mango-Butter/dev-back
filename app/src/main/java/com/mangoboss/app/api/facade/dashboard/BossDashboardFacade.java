package com.mangoboss.app.api.facade.dashboard;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.dashboard.response.StaffAttendanceCountResponse;
import com.mangoboss.app.dto.dashboard.response.StaffAttendanceSummaryResponse;
import com.mangoboss.storage.attendance.projection.StaffAttendanceCountProjection;
import com.mangoboss.storage.schedule.RegularGroupEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BossDashboardFacade {

    private final StoreService storeService;
    private final StaffService staffService;
    private final ScheduleService scheduleService;
    private final AttendanceService attendanceService;

    public List<StaffAttendanceSummaryResponse> getStaffAttendanceDashboard(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);

        final List<StaffEntity> staffList = staffService.getStaffsForStore(storeId);
        final List<Long> staffIds = staffList.stream()
                .map(StaffEntity::getId)
                .toList();

        final Map<Long, StaffAttendanceCountProjection> attendanceMap = attendanceService.getAttendanceCountsByStaffIds(staffIds)
                .stream()
                .collect(Collectors.toMap(StaffAttendanceCountProjection::getStaffId, projection -> projection));

        final Map<Long, List<DayOfWeek>> workDaysMap = scheduleService.getRegularGroupsByStaffList(staffList)
                .stream()
                .collect(Collectors.groupingBy(
                        group -> group.getStaff().getId(),
                        Collectors.mapping(RegularGroupEntity::getDayOfWeek, Collectors.toList())
                ));

        return staffList.stream()
                .map(staff -> {
                    final Long staffId = staff.getId();
                    final List<DayOfWeek> workDays = workDaysMap.getOrDefault(staffId, List.of());
                    final StaffAttendanceCountProjection projection = attendanceMap.get(staffId);
                    return StaffAttendanceSummaryResponse.of(
                            staff,
                            workDays,
                            StaffAttendanceCountResponse.of(projection)
                    );
                })
                .toList();
    }
}