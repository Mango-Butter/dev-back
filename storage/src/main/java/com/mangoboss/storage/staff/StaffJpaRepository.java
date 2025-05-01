package com.mangoboss.storage.staff;

import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffJpaRepository extends JpaRepository<StaffEntity, Long> {
    Boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    Optional<StaffEntity> findByIdAndStoreId(Long id, Long storeId);

    List<StaffEntity> findAllByStoreId(Long storeId);

    Optional<StaffEntity> findByUserAndStore(UserEntity user, StoreEntity store);
}
