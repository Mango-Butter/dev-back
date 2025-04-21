package com.mangoboss.storage.staff;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffJpaRepository extends JpaRepository<StaffEntity, Long> {
    Boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    Optional<StaffEntity> findByIdAndStoreId(Long staffId, Long storeId);
}
