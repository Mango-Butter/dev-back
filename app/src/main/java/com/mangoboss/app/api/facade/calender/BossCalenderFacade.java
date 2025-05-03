package com.mangoboss.app.api.facade.calender;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.calender.WorkDotResponse;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.projection.WorkDotProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BossCalenderFacade {
    private final AttendanceService attendanceService;
    private final StoreService storeService;

    public List<WorkDotResponse> getWorkDots(final Long storeId, final Long bossId, final LocalDate start, final LocalDate end) {
        storeService.isBossOfStore(storeId, bossId);
        List<WorkDotProjection> workDotProjections = attendanceService.getWorkDots(storeId, start, end);
        return workDotProjections.stream().map(WorkDotResponse::of).toList();
    }
}
