package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;

import java.util.List;

public interface StaffRepository {
    StaffEntity save(StaffEntity staff);

    Boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    StaffEntity getByIdAndStoreId(Long id, Long storeId);

    List<StaffEntity> findAllByStoreId(Long storeId);

    StaffEntity getByUserAndStore(UserEntity user, StoreEntity store);
}
