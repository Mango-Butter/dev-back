package com.mangoboss.admin.domain.repository;

import com.mangoboss.storage.faq.FaqEntity;
import com.mangoboss.storage.faq.FaqCategory;

import java.util.List;

public interface FaqRepository {
    FaqEntity save(FaqEntity faqEntity);

    void delete(FaqEntity faqEntity);

    FaqEntity findById(Long faqId);

    List<FaqEntity> findAll();

    List<FaqEntity> findByCategory(FaqCategory category);
}
