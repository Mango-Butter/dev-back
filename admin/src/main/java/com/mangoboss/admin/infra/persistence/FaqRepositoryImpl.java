package com.mangoboss.admin.infra.persistence;

import com.mangoboss.admin.common.exception.CustomErrorInfo;
import com.mangoboss.admin.common.exception.CustomException;
import com.mangoboss.admin.domain.repository.FaqRepository;
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
    public FaqEntity save(FaqEntity faqEntity) {
        return faqJpaRepository.save(faqEntity);
    }

    @Override
    public void delete(FaqEntity faqEntity) {
        faqJpaRepository.delete(faqEntity);
    }

    @Override
    public FaqEntity findById(Long faqId) {
        return faqJpaRepository.findById(faqId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.FAQ_NOT_FOUND));
    }

    @Override
    public List<FaqEntity> findAll() {
        return faqJpaRepository.findAll();
    }

    @Override
    public List<FaqEntity> findByCategory(FaqCategory category) {
        return faqJpaRepository.findByCategory(category);
    }
}
