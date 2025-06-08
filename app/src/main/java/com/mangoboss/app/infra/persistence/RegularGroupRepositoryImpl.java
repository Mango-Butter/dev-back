package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.RegularGroupRepository;
import com.mangoboss.storage.schedule.RegularGroupEntity;
import com.mangoboss.storage.schedule.RegularGroupJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RegularGroupRepositoryImpl implements RegularGroupRepository {
    private final RegularGroupJpaRepository regularGroupJpaRepository;

    @Override
    public RegularGroupEntity save(final RegularGroupEntity regularGroup) {
        return regularGroupJpaRepository.save(regularGroup);
    }

    @Override
    public List<RegularGroupEntity> findActiveOrUpcomingByStaffId(final Long staffId, final LocalDate date) {
        return regularGroupJpaRepository.findActiveOrUpcomingByStaffId(staffId, date);
    }

    @Override
    public RegularGroupEntity getById(Long id) {
        return regularGroupJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.REGULAR_GROUP_NOT_FOUND));
    }

    @Override
    public void delete(RegularGroupEntity regularGroup) {
        regularGroupJpaRepository.delete(regularGroup);
    }
}
