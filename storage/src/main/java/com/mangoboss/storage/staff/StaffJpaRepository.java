package com.mangoboss.storage.staff;

import java.util.Optional;

import com.mangoboss.storage.store.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mangoboss.storage.user.UserEntity;

public interface StaffJpaRepository extends JpaRepository<StaffEntity, Long> {
    Boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    Optional<StaffEntity> findByIdAndStoreId(Long staffId, Long storeId);
	Optional<StaffEntity> findByUserAndStore(UserEntity user, StoreEntity store);
}
