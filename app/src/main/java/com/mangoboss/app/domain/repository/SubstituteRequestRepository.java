package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.schedule.SubstituteRequestEntity;

import java.util.List;

public interface SubstituteRequestRepository {
    SubstituteRequestEntity save(SubstituteRequestEntity substituteRequest);

    List<SubstituteRequestEntity> findAllByRequesterStaffIdOrTargetStaffId(Long staffId);

    Boolean existsByRequestScheduleId(Long requestScheduleId);
}
