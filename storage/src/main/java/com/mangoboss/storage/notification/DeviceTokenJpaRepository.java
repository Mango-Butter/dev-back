package com.mangoboss.storage.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceTokenJpaRepository extends JpaRepository<DeviceTokenEntity, Long> {

    List<DeviceTokenEntity> findByUserIdAndIsDeletedFalse(Long userId);

    boolean existsByUserIdAndTokenValue(Long userId, String tokenValue);

    @Modifying
    @Query("UPDATE DeviceTokenEntity d SET d.isDeleted = false, d.modifiedAt = CURRENT_TIMESTAMP WHERE d.userId = :userId AND d.tokenValue = :tokenValue")
    void updateIsDeletedFalseAndModifiedAt(@Param("userId") Long userId, @Param("tokenValue") String tokenValue);
}