package com.mangoboss.app.api.facade.staff;

import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.staff.request.RegularGroupCreateRequest;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffStaffFacade {
    private final ScheduleService scheduleService;
    private final StaffService staffService;
    private final StoreService storeService;

    public void createRegularSchedules(final Long storeId, final Long staffId, final Long bossId, final List<RegularGroupCreateRequest> requestList) {
        storeService.isBossOfStore(storeId, bossId);

        final StaffEntity staff = staffService.getStaffBelongsToStore(storeId, staffId);
        requestList.forEach(request -> scheduleService.createRegularGroup(request.toEntity(staff)));
    }
}
