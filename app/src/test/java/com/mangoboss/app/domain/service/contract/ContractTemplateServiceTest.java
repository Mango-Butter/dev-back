package com.mangoboss.app.domain.service.contract;

import com.mangoboss.app.domain.repository.ContractTemplateRepository;
import com.mangoboss.app.dto.contract.request.ContractTemplateData;
import com.mangoboss.storage.contract.ContractTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractTemplateServiceTest {

    @Mock
    private ContractTemplateRepository contractTemplateRepository;

    @InjectMocks
    private ContractTemplateService contractTemplateService;

    @Test
    void 템플릿을_생성할_수_있다() {
        // given
        ContractTemplate template = mock(ContractTemplate.class);
        when(contractTemplateRepository.save(template)).thenReturn(template);

        // when
        ContractTemplate result = contractTemplateService.createContractTemplate(template);

        // then
        assertThat(result).isEqualTo(template);
    }

    @Test
    void 템플릿을_조회할_수_있다() {
        // given
        Long storeId = 1L;
        List<ContractTemplate> templates = List.of(mock(ContractTemplate.class));
        when(contractTemplateRepository.findAllByStoreId(storeId)).thenReturn(templates);

        // when
        List<ContractTemplate> result = contractTemplateService.getAllContractTemplates(storeId);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 템플릿을_업데이트할_수_있다() {
        // given
        Long storeId = 1L;
        Long templateId = 1L;
        String title = "updated-title";
        ContractTemplateData contractTemplateData = mock(ContractTemplateData.class);

        ContractTemplate template = mock(ContractTemplate.class);
        when(contractTemplateRepository.getByIdAndStoreId(templateId, storeId)).thenReturn(template);
        when(template.update(eq(title), any())).thenReturn(template);

        // when
        ContractTemplate updated = contractTemplateService.updateContractTemplate(storeId, templateId, title, contractTemplateData);

        // then
        assertThat(updated).isEqualTo(template);
    }

    @Test
    void 템플릿을_삭제할_수_있다() {
        // given
        Long storeId = 1L;
        Long templateId = 10L;
        ContractTemplate template = mock(ContractTemplate.class);

        when(contractTemplateRepository.getByIdAndStoreId(templateId, storeId)).thenReturn(template);

        // when
        contractTemplateService.deleteContractTemplate(storeId, templateId);

        // then
        verify(contractTemplateRepository).delete(template);
    }
}
