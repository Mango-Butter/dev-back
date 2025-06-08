package com.mangoboss.admin.domain.service;

import com.mangoboss.admin.domain.repository.FaqRepository;
import com.mangoboss.storage.faq.FaqEntity;
import com.mangoboss.storage.faq.FaqCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqService {

    private final FaqRepository faqRepository;

    @Transactional
    public void createFaq(final FaqEntity faq) {
        faqRepository.save(faq);
    }

    @Transactional
    public void updateFaq(final Long faqId, final FaqCategory category, final String question, final String answer) {
        FaqEntity faq = faqRepository.findById(faqId);
        faq.update(question, answer, category);
    }

    @Transactional
    public void deleteFaq(final Long faqId) {
        FaqEntity faq = faqRepository.findById(faqId);
        faqRepository.delete(faq);
    }

    public List<FaqEntity> getFaqs(final FaqCategory category) {
        if (category == FaqCategory.ALL) {
            return faqRepository.findAll();
        }
        return faqRepository.findByCategory(category);
    }
}