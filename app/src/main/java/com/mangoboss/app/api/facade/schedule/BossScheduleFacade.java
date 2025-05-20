package com.mangoboss.app.api.facade.schedule;

import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.schedule.request.ScheduleCreateRequest;
import com.mangoboss.app.dto.schedule.request.ScheduleUpdateRequest;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BossScheduleFacade {
    private final ScheduleService scheduleService;
    private final StaffService staffService;
    private final StoreService storeService;

    public void createSchedule(final Long storeId, final Long bossId, final ScheduleCreateRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        scheduleService.validateTime(request.startTime(), request.endTime());
        scheduleService.validateScheduleCreatable(request.workDate(), request.startTime());
        final StaffEntity staff = staffService.validateStaffBelongsToStore(storeId, request.staffId());
        scheduleService.createSchedule(request.toEntity(staff, storeId));
    }

    public void deleteSchedule(final Long storeId, final Long bossId, final Long scheduleId) {
        storeService.isBossOfStore(storeId, bossId);
        scheduleService.deleteScheduleById(scheduleId);
    }

    public void updateSchedule(final Long storeId, final Long scheduleId, final Long bossId, final ScheduleUpdateRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        scheduleService.validateTime(request.startTime(), request.endTime());
        scheduleService.validateScheduleCreatable(request.workDate(), request.startTime());
        scheduleService.updateSchedule(scheduleId, request.workDate(), request.startTime(), request.endTime());
    }
}
