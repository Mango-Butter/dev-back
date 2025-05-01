package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.staff.StaffEntity;

import java.util.List;

public interface StaffRepository {
    StaffEntity save(StaffEntity staff);

    Boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    StaffEntity getByIdAndStoreId(Long id, Long storeId);

    List<StaffEntity> findAllByStoreId(Long storeId);
}
