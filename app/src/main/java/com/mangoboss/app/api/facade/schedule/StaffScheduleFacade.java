package com.mangoboss.app.api.facade.schedule;

import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.schedule.SubstituteRequestService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.schedule.request.SubstituteRequestRequest;
import com.mangoboss.app.dto.schedule.response.SubstituteRequestResponse;
import com.mangoboss.app.dto.staff.response.SubstituteCandidateResponse;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.schedule.SubstituteRequestEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffScheduleFacade {
    private final StaffService staffService;
    private final ScheduleService scheduleService;
    private final SubstituteRequestService substituteRequestService;

    public List<SubstituteCandidateResponse> getSubstituteCandidates(final Long storeId, final Long userId, final Long scheduleId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final ScheduleEntity schedule = scheduleService.validateScheduleBelongsToStaff(scheduleId, staff.getId());
        List<StaffEntity> storeStaffs = staffService.getStaffsForStore(storeId);
        return storeStaffs.stream()
                .filter(storeStaff -> !storeStaff.getUser().getId().equals(userId))
                .map(storeStaff ->
                        SubstituteCandidateResponse.of(
                                storeStaff,
                                scheduleService.isSubstituteCandidate(storeStaff.getId(), schedule))
                )
                .toList();
    }

    public void requestSubstitution(final Long storeId, final Long userId, final Long scheduleId, final SubstituteRequestRequest request) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final StaffEntity target = staffService.validateStaffBelongsToStore(storeId, request.targetStaffId());
        final ScheduleEntity schedule = scheduleService.validateScheduleBelongsToStaff(scheduleId, staff.getId());
        substituteRequestService.createSubstituteRequest(
                schedule,
                request.reason(),
                staff,
                target
        );
    }

    public List<SubstituteRequestResponse> getSubstituteRequests(final Long storeId, final Long userId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        List<SubstituteRequestEntity> substituteRequests = substituteRequestService.getSubstituteRequestsForStaff(staff.getId());
        return substituteRequests.stream()
                .map(SubstituteRequestResponse::fromEntity)
                .toList();
    }
}
