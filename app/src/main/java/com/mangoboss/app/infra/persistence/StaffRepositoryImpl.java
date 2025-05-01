package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.StaffRepository;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.staff.StaffJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public StaffEntity getByIdAndStoreId(final Long staffId, final Long storeId) {
        return staffJpaRepository.findByIdAndStoreId(staffId,storeId)
                .orElseThrow(()->new CustomException(CustomErrorInfo.STAFF_NOT_FOUND));
    }

    @Override
    public List<StaffEntity> findAllByStoreId(Long storeId) {
        return staffJpaRepository.findAllByStoreId(storeId);
    }
}
