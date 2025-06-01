package com.mangoboss.app.domain.service.schedule;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.ScheduleRepository;
import com.mangoboss.app.domain.repository.SubstituteRequestRepository;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.schedule.SubstituteRequestEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SubstituteRequestService {
    private final SubstituteRequestRepository substituteRequestRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void createSubstituteRequest(final ScheduleEntity schedule, final String reason, final StaffEntity requester, final StaffEntity target) {
        isAlreadyRequested(schedule.getId());
        schedule.requested();
        SubstituteRequestEntity substituteRequest = SubstituteRequestEntity.create(
                reason,
                requester,
                target,
                schedule
        );
        substituteRequestRepository.save(substituteRequest);
    }

    private void isAlreadyRequested(final Long scheduleId) {
        if (substituteRequestRepository.existsByRequestScheduleId(scheduleId)) {
            throw new CustomException(CustomErrorInfo.SCHEDULE_ALREADY_SUBSTITUTED);
        }
    }

    public List<SubstituteRequestEntity> getSubstituteRequestsForStaff(final Long staffId) {
        return substituteRequestRepository.findAllByRequesterStaffIdOrTargetStaffId(staffId);
    }

    public List<SubstituteRequestEntity> getSubstituteRequestsForStore(final Long storeId) {
        return substituteRequestRepository.findAllByStoreId(storeId);
    }

    public SubstituteRequestEntity getSubstituteRequestById(final Long substitutionId) {
        return substituteRequestRepository.getById(substitutionId);
    }

    @Transactional
    public void approveSubstitution(final SubstituteRequestEntity substituteRequest, final StaffEntity target) {
        ScheduleEntity schedule = scheduleRepository.getById(substituteRequest.getRequestScheduleId());
        schedule.substituted(target);
        substituteRequest.approved();
    }

    @Transactional
    public void rejectSubstitution(final Long substitutionId) {
        SubstituteRequestEntity substituteRequest = substituteRequestRepository.getById(substitutionId);
        ScheduleEntity schedule = scheduleRepository.getById(substituteRequest.getRequestScheduleId());
        schedule.rejected();
        substituteRequest.rejected();
    }
}
