package com.mangoboss.app.domain.service.faq;

import com.mangoboss.app.domain.repository.FaqRepository;
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

    public List<FaqEntity> getFaqs(final FaqCategory category) {
        if (category == FaqCategory.ALL) {
            return faqRepository.findAll();
        }
        return faqRepository.findByCategory(category);
    }
}