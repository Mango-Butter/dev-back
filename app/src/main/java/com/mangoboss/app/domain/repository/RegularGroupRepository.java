package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.schedule.RegularGroupEntity;

import java.util.List;

public interface RegularGroupRepository {

    RegularGroupEntity save(RegularGroupEntity repeatGroup);

    List<RegularGroupEntity> findAllByStaffId(Long storeId);
}
