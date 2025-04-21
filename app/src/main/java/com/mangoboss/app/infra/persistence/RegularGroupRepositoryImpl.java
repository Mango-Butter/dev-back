package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.RegularGroupRepository;
import com.mangoboss.storage.schedule.RegularGroupEntity;
import com.mangoboss.storage.schedule.RegularGroupJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RegularGroupRepositoryImpl implements RegularGroupRepository {
    private final RegularGroupJpaRepository regularGroupJpaRepository;

    @Override
    public RegularGroupEntity save(final RegularGroupEntity repeatGroup){
        return regularGroupJpaRepository.save(repeatGroup);
    }
}
