package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.FaqRepository;
import com.mangoboss.storage.faq.FaqCategory;
import com.mangoboss.storage.faq.FaqEntity;
import com.mangoboss.storage.faq.FaqJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FaqRepositoryImpl implements FaqRepository {

    private final FaqJpaRepository faqJpaRepository;

    @Override
    public List<FaqEntity> findAll() {
        return faqJpaRepository.findAll();
    }

    @Override
    public List<FaqEntity> findByCategory(FaqCategory category) {
        return faqJpaRepository.findByCategory(category);
    }
}
