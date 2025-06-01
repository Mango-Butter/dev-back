package com.mangoboss.app.api.facade.schedule;

import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.schedule.SubstituteRequestService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.schedule.request.ScheduleCreateRequest;
import com.mangoboss.app.dto.schedule.request.ScheduleUpdateRequest;
import com.mangoboss.app.dto.schedule.response.SubstituteRequestResponse;
import com.mangoboss.storage.schedule.SubstituteRequestEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BossScheduleFacade {
    private final ScheduleService scheduleService;
    private final SubstituteRequestService substituteRequestService;
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

    public List<SubstituteRequestResponse> getSubstituteRequests(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        return substituteRequestService.getSubstituteRequestsForStore(storeId)
                .stream()
                .map(SubstituteRequestResponse::fromEntity)
                .toList();
    }

    public void approveSubstituteRequest(final Long storeId, final Long bossId, final Long substitutionId) {
        storeService.isBossOfStore(storeId, bossId);
        SubstituteRequestEntity substituteRequest = substituteRequestService.getSubstituteRequestById(substitutionId);
        StaffEntity target = staffService.validateStaffBelongsToStore(storeId, substituteRequest.getTargetStaffId());
        substituteRequestService.approveSubstitution(substituteRequest, target);
    }

    public void rejectSubstituteRequest(final Long storeId, final Long bossId, final Long substitutionId) {
        storeService.isBossOfStore(storeId, bossId);
        substituteRequestService.rejectSubstitution(substitutionId);
    }
}
