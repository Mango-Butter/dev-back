package com.mangoboss.admin.infra.persistence;

import com.mangoboss.admin.domain.repository.StaffRepository;
import com.mangoboss.storage.staff.StaffJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class StaffRepositoryImpl implements StaffRepository {

    private final StaffJpaRepository staffJpaRepository;

    @Override
    public Long countByCreatedAtBetween(final LocalDateTime start, final LocalDateTime end) {
        return staffJpaRepository.countByCreatedAtBetween(start, end);
    }

    @Override
    public Long countByUserId(final Long userId) {
        return staffJpaRepository.countByBossId(userId);
    }
}