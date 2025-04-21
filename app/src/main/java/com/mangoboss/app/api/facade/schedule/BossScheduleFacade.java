package com.mangoboss.app.api.facade.schedule;

import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.schedule.request.ScheduleCreateRequest;
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

        final StaffEntity staff = staffService.getStaffBelongsToStore(storeId, request.staffId());
        scheduleService.createSchedule(request.toEntity(staff));
    }
}
