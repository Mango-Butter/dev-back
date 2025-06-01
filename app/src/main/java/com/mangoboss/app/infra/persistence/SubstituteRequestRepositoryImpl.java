package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.SubstituteRequestRepository;
import com.mangoboss.storage.schedule.SubstituteRequestEntity;
import com.mangoboss.storage.schedule.SubstituteRequestJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubstituteRequestRepositoryImpl implements SubstituteRequestRepository {
    private final SubstituteRequestJpaRepository substituteRequestJpaRepository;


    @Override
    public SubstituteRequestEntity save(final SubstituteRequestEntity substituteRequest) {
        return substituteRequestJpaRepository.save(substituteRequest);
    }

    @Override
    public List<SubstituteRequestEntity> findAllByRequesterStaffIdOrTargetStaffId(final Long staffId) {
        return substituteRequestJpaRepository.findAllByRequesterStaffIdOrTargetStaffId(staffId, staffId);
    }

    @Override
    public Boolean existsByRequestScheduleId(final Long requestScheduleId) {
        return substituteRequestJpaRepository.existsByRequestScheduleId(requestScheduleId);
    }
}
