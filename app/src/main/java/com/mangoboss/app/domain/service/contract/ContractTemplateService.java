package com.mangoboss.app.domain.service.contract;

import com.mangoboss.app.common.util.JsonConverter;
import com.mangoboss.app.domain.repository.ContractTemplateRepository;
import com.mangoboss.app.dto.contract.request.ContractTemplateData;
import com.mangoboss.storage.contract.ContractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractTemplateService {

    private final ContractTemplateRepository contractTemplateRepository;

    @Transactional
    public ContractTemplate createContractTemplate(final ContractTemplate template) {
        return contractTemplateRepository.save(template);
    }

    public List<ContractTemplate> getAllContractTemplates(final Long storeId) {
        return contractTemplateRepository.findAllByStoreId(storeId);
    }

    public ContractTemplate getContractTemplateById(final Long storeId, final Long templateId) {
        return contractTemplateRepository.getByIdAndStoreId(templateId, storeId);
    }

    @Transactional
    public ContractTemplate updateContractTemplate(final Long storeId, final Long templateId, final String title, final ContractTemplateData contractTemplateData) {
        final ContractTemplate existingContractTemplate = getContractTemplateById(storeId, templateId);
        return existingContractTemplate.update(title, JsonConverter.toJson(contractTemplateData));
    }

    @Transactional
    public void deleteContractTemplate(final Long storeId, final Long templateId) {
        final ContractTemplate template = getContractTemplateById(storeId, templateId);
        contractTemplateRepository.delete(template);
    }
}