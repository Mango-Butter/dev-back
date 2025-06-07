package com.mangoboss.admin.infra.persistence;

import com.mangoboss.admin.domain.repository.StoreRepository;
import com.mangoboss.storage.store.StoreJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {

    private final StoreJpaRepository storeJpaRepository;

    @Override
    public Long countByCreatedAtBetween(final LocalDateTime start, final LocalDateTime end) {
        return storeJpaRepository.countByCreatedAtBetween(start, end);
    }
}