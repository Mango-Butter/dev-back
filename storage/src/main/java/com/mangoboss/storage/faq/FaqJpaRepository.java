package com.mangoboss.storage.faq;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqJpaRepository extends JpaRepository<FaqEntity, Long> {

    List<FaqEntity> findByCategory(FaqCategory category);
}
