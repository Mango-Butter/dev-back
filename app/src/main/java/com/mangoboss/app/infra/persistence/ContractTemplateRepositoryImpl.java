package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.ContractTemplateRepository;
import com.mangoboss.storage.contract.ContractTemplate;
import com.mangoboss.storage.contract.ContractTemplateJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContractTemplateRepositoryImpl implements ContractTemplateRepository {

    private final ContractTemplateJpaRepository contractTemplateJpaRepository;

    @Override
    public ContractTemplate save(final ContractTemplate template) {
        return contractTemplateJpaRepository.save(template);
    }

    @Override
    public List<ContractTemplate> findAllByStoreId(final Long storeId) {
        return contractTemplateJpaRepository.findAllByStoreId(storeId);
    }

    @Override
    public ContractTemplate getByIdAndStoreId(final Long templateId, final Long storeId) {
        return contractTemplateJpaRepository.findByIdAndStoreId(templateId, storeId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.CONTRACT_TEMPLATE_NOT_FOUND));
    }

    @Override
    public void delete(final ContractTemplate template) {
        contractTemplateJpaRepository.delete(template);
    }
}