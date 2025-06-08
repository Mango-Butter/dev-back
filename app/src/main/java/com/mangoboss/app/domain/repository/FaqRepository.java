package com.mangoboss.app.domain.repository;


import com.mangoboss.storage.faq.FaqEntity;
import com.mangoboss.storage.faq.FaqCategory;

import java.util.List;

public interface FaqRepository {
    List<FaqEntity> findAll();

    List<FaqEntity> findByCategory(FaqCategory category);
}
