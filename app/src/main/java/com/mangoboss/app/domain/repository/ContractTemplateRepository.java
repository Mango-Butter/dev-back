package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.contract.ContractTemplate;

import java.util.List;

public interface ContractTemplateRepository {
    ContractTemplate save(final ContractTemplate template);

    List<ContractTemplate> findAllByStoreId(final Long storeId);

    ContractTemplate getByIdAndStoreId(final Long templateId, final Long storeId);

    void delete(final ContractTemplate template);
}