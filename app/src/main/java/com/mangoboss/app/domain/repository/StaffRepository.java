package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;

public interface StaffRepository {
    StaffEntity save(StaffEntity staff);

    Boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    StaffEntity getByIdAndStoreId(Long staffId, Long storeId);
    StaffEntity getByUserAndStore(UserEntity user, StoreEntity store);
}
