package com.mangoboss.storage.contract;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractJpaRepository extends JpaRepository<ContractEntity, Long> {
    List<ContractEntity> findAllByStaffId(Long staffId);

    @Query("""
        SELECT c
        FROM ContractEntity c
        JOIN StaffEntity s ON c.staffId = s.id
        WHERE s.store.id = :storeId
    """)
    List<ContractEntity> findAllByStoreId(@Param("storeId") Long storeId);

}
