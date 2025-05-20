package com.mangoboss.app.api.facade.calendar;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.calender.WorkWithStaffResponse;
import com.mangoboss.app.dto.calender.WorkDotResponse;
import com.mangoboss.storage.attendance.projection.WorkDotProjection;
import com.mangoboss.storage.schedule.ScheduleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffCalendarFacade {
    private final AttendanceService attendanceService;
    private final ScheduleService scheduleService;
    private final StaffService staffService;

    public List<WorkDotResponse> getWorkDots(final Long storeId, final Long userId, final LocalDate start, final LocalDate end) {
        staffService.getVerifiedStaff(userId, storeId);
        List<WorkDotProjection> workDotProjections = attendanceService.getWorkDots(storeId, start, end);
        return workDotProjections.stream().map(WorkDotResponse::of).toList();
    }

    public List<WorkWithStaffResponse> getDailyWorks(final Long storeId, final Long userId, final LocalDate date) {
        staffService.getVerifiedStaff(userId, storeId);
        List<ScheduleEntity> schedules = scheduleService.getDailySchedules(storeId, date);
        return schedules.stream().map(WorkWithStaffResponse::of).toList();
    }
}
