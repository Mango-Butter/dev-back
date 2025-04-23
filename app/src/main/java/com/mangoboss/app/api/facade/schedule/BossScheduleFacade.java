package com.mangoboss.app.api.facade.schedule;

import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.schedule.request.ScheduleCreateRequest;
import com.mangoboss.app.dto.schedule.response.ScheduleDailyResponse;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BossScheduleFacade {
    private final ScheduleService scheduleService;
    private final StaffService staffService;
    private final StoreService storeService;

    public void createSchedule(final Long storeId, final Long bossId, final ScheduleCreateRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        scheduleService.validateTimeOrder(request.startTime(), request.endTime());
        final StaffEntity staff = staffService.getStaffBelongsToStore(storeId, request.staffId());
        scheduleService.createSchedule(request.toEntity(staff));
    }

    public List<ScheduleDailyResponse> getDailySchedule(final Long storeId, final Long bossId, final LocalDate date) {
        storeService.isBossOfStore(storeId, bossId);
        List<ScheduleEntity> schedules = scheduleService.getDailySchedules(storeId, date);
        return schedules.stream().map(ScheduleDailyResponse::fromEntity).toList();
    }
}
