package com.mangoboss.admin.domain.repository;

import java.time.LocalDateTime;

public interface StaffRepository {
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
