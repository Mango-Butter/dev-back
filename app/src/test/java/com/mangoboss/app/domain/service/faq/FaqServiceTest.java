package com.mangoboss.app.domain.service.faq;

import com.mangoboss.app.domain.repository.FaqRepository;
import com.mangoboss.storage.faq.FaqCategory;
import com.mangoboss.storage.faq.FaqEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FaqServiceTest {

    @InjectMocks
    private FaqService faqService;

    @Mock
    private FaqRepository faqRepository;

    @Test
    void 전체_카테고리를_조회할_때_모든_FAQ를_가져온다() {
        // given
        List<FaqEntity> allFaqs = List.of(mock(FaqEntity.class), mock(FaqEntity.class));
        when(faqRepository.findAll()).thenReturn(allFaqs);

        // when
        List<FaqEntity> result = faqService.getFaqs(FaqCategory.ALL);

        // then
        assertThat(result).isEqualTo(allFaqs);
        verify(faqRepository).findAll();
        verify(faqRepository, never()).findByCategory(any());
    }

    @Test
    void 서비스_카테고리를_조회할_때_SERVICE_FAQ만_가져온다() {
        // given
        List<FaqEntity> serviceFaqs = List.of(mock(FaqEntity.class));
        when(faqRepository.findByCategory(FaqCategory.SERVICE)).thenReturn(serviceFaqs);

        // when
        List<FaqEntity> result = faqService.getFaqs(FaqCategory.SERVICE);

        // then
        assertThat(result).isEqualTo(serviceFaqs);
        verify(faqRepository).findByCategory(FaqCategory.SERVICE);
        verify(faqRepository, never()).findAll();
    }

    @Test
    void 결제_카테고리를_조회할_때_PAYMENT_FAQ만_가져온다() {
        // given
        List<FaqEntity> paymentFaqs = List.of(mock(FaqEntity.class));
        when(faqRepository.findByCategory(FaqCategory.PAYMENT)).thenReturn(paymentFaqs);

        // when
        List<FaqEntity> result = faqService.getFaqs(FaqCategory.PAYMENT);

        // then
        assertThat(result).isEqualTo(paymentFaqs);
        verify(faqRepository).findByCategory(FaqCategory.PAYMENT);
        verify(faqRepository, never()).findAll();
    }

    @Test
    void 계정_카테고리를_조회할_때_ACCOUNT_FAQ만_가져온다() {
        // given
        List<FaqEntity> accountFaqs = List.of(mock(FaqEntity.class));
        when(faqRepository.findByCategory(FaqCategory.ACCOUNT)).thenReturn(accountFaqs);

        // when
        List<FaqEntity> result = faqService.getFaqs(FaqCategory.ACCOUNT);

        // then
        assertThat(result).isEqualTo(accountFaqs);
        verify(faqRepository).findByCategory(FaqCategory.ACCOUNT);
        verify(faqRepository, never()).findAll();
    }
}