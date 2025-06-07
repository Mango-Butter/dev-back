package com.mangoboss.admin.domain.repository;

import java.time.LocalDateTime;

public interface StoreRepository {
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}