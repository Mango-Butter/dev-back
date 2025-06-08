package com.mangoboss.app.api.facade.calendar;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.store.StoreService;
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
public class BossCalendarFacade {
    private final AttendanceService attendanceService;
    private final ScheduleService scheduleService;
    private final StoreService storeService;

    public List<WorkDotResponse> getWorkDots(final Long storeId, final Long bossId, final LocalDate start, final LocalDate end) {
        storeService.isBossOfStore(storeId, bossId);
        List<WorkDotProjection> workDotProjections = attendanceService.getWorkDots(storeId, start, end);
        return workDotProjections.stream().map(WorkDotResponse::of).toList();
    }

    public List<WorkWithStaffResponse> getDailyWorks(final Long storeId, final Long bossId, final LocalDate date) {
        storeService.isBossOfStore(storeId, bossId);
        List<ScheduleEntity> schedules = scheduleService.getDailySchedules(storeId, date);
        return schedules.stream().map(WorkWithStaffResponse::of).toList();
    }
}
