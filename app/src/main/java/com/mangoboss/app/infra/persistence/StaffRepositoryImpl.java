package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.StaffRepository;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.staff.StaffJpaRepository;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;
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

    @Override
    public StaffEntity getByIdAndStoreId(final Long staffId, final Long storeId) {
        return staffJpaRepository.findByIdAndStoreId(staffId,storeId)
                .orElseThrow(()->new CustomException(CustomErrorInfo.STAFF_NOT_FOUND));
    }

    @Override
    public StaffEntity getByUserAndStore(final UserEntity user, final StoreEntity store) {
        return staffJpaRepository.findByUserAndStore(user, store)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.STAFF_NOT_FOUND));
    }
}
