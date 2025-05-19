package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.contract.ContractEntity;

import java.util.List;

public interface ContractRepository {
    ContractEntity save(final ContractEntity contract);

    ContractEntity getContractById(final Long contractId);

    List<ContractEntity> findAllByStaffId(Long staffId);

    void delete(final ContractEntity contract);

    List<ContractEntity> findAllByStoreId(Long storeId);
}
