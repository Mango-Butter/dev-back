package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.schedule.RegularGroupEntity;

import java.time.LocalDate;
import java.util.List;

public interface RegularGroupRepository {

    RegularGroupEntity save(RegularGroupEntity regularGroup);

    List<RegularGroupEntity> findActiveOrUpcomingByStaffId(Long storeId, LocalDate date);

    RegularGroupEntity getById(Long id);

    void delete(RegularGroupEntity regularGroup);
}
