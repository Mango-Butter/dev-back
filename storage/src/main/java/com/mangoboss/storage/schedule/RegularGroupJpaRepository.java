package com.mangoboss.storage.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegularGroupJpaRepository extends JpaRepository<RegularGroupEntity, Long> {
    List<RegularGroupEntity> findAllByStaffId(Long staffId);

    List<RegularGroupEntity> findAllByStaffIdIn(List<Long> staffIds);
}
