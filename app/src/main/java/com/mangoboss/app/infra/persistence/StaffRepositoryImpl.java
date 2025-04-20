package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.StaffRepository;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.staff.StaffJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StaffRepositoryImpl implements StaffRepository {
    private final StaffJpaRepository staffJpaRepository;

    @Override
    public StaffEntity save(final StaffEntity staff) {
        return staffJpaRepository.save(staff);
    }

    @Override
    public Boolean existsByUserIdAndStoreId(final Long userId, final Long storeId) {
        return staffJpaRepository.existsByUserIdAndStoreId(userId, storeId);
    }
}
