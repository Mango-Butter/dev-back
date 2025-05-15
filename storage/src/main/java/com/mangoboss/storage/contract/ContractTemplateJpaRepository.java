package com.mangoboss.storage.contract;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContractTemplateJpaRepository extends JpaRepository<ContractTemplate, Long> {
    List<ContractTemplate> findAllByStoreId(Long storeId);

    Optional<ContractTemplate> findByIdAndStoreId(Long id, Long storeId);
}