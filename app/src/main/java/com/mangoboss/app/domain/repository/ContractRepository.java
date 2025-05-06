package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.contract.ContractEntity;

public interface ContractRepository {
    ContractEntity save(final ContractEntity contract);

    ContractEntity getContractById(final Long contractId);
}
