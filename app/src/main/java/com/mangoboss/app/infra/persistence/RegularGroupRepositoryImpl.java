package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.RegularGroupRepository;
import com.mangoboss.storage.schedule.RegularGroupEntity;
import com.mangoboss.storage.schedule.RegularGroupJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RegularGroupRepositoryImpl implements RegularGroupRepository {
    private final RegularGroupJpaRepository regularGroupJpaRepository;

    @Override
    public RegularGroupEntity save(final RegularGroupEntity repeatGroup){
        return regularGroupJpaRepository.save(repeatGroup);
    }

    @Override
    public List<RegularGroupEntity> findAllByStaffId(final Long staffId) {
        return regularGroupJpaRepository.findAllByStaffId(staffId);
    }
}
