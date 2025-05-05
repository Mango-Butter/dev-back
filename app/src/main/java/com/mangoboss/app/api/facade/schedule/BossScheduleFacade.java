package com.mangoboss.app.api.facade.schedule;

import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.schedule.request.ScheduleCreateRequest;
import com.mangoboss.app.dto.schedule.request.ScheduleUpdateRequest;
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
        scheduleService.validateTime(request.startTime(), request.endTime());
        scheduleService.validateScheduleCreatable(request.toStartDateTime());
        final StaffEntity staff = staffService.getStaffBelongsToStore(storeId, request.staffId());
        scheduleService.createSchedule(request.toEntity(staff, storeId));
    }

    // todo 삭제해야 함
    public List<ScheduleDailyResponse> getDailySchedule(final Long storeId, final Long bossId, final LocalDate date) {
        storeService.isBossOfStore(storeId, bossId);
        List<ScheduleEntity> schedules = scheduleService.getDailySchedules(storeId, date);
        return schedules.stream().map(ScheduleDailyResponse::fromEntity).toList();
    }

    public void deleteSchedule(final Long storeId, final Long bossId, final Long scheduleId) {
        storeService.isBossOfStore(storeId, bossId);
        scheduleService.deleteScheduleById(scheduleId);
    }

    public void updateSchedule(final Long storeId, final Long scheduleId, final Long bossId, final ScheduleUpdateRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        scheduleService.validateTime(request.startTime(), request.endTime());
        scheduleService.validateScheduleCreatable(request.toStartDateTime());
        scheduleService.updateSchedule(scheduleId, request.workDate(), request.toStartDateTime(), request.toEndDateTime());
    }
}
