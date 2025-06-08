package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.SubstituteRequestRepository;
import com.mangoboss.storage.schedule.SubstituteRequestEntity;
import com.mangoboss.storage.schedule.SubstituteRequestJpaRepository;
import com.mangoboss.storage.schedule.SubstituteRequestState;
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

    @Override
    public List<SubstituteRequestEntity> findAllByStoreId(final Long storeId) {
        return substituteRequestJpaRepository.findAllByStoreId(storeId);
    }

    @Override
    public SubstituteRequestEntity getById(final Long id) {
        return substituteRequestJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.SUBSTITUTE_REQUEST_NOT_FOUND));
    }

    @Override
    public List<SubstituteRequestEntity> findRecentIncompleteRequestsByStoreId(final Long storeId) {
        return substituteRequestJpaRepository.findAllByStoreIdAndRequestStateOrderByCreatedAtDesc(storeId, SubstituteRequestState.PENDING);
    }
}
