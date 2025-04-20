package com.mangoboss.storage.staff;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffJpaRepository extends JpaRepository<StaffEntity, Long> {
    Boolean existsByUserIdAndStoreId(Long userId, Long storeId);
}
