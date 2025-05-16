package com.mangoboss.storage.contract;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractJpaRepository extends JpaRepository<ContractEntity, Long> {
    List<ContractEntity> findAllByStaffId(Long staffId);
}
