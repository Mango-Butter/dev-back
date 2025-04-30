package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.schedule.RegularGroupEntity;

import java.util.List;

public interface RegularGroupRepository {

    RegularGroupEntity save(RegularGroupEntity regularGroup);

    List<RegularGroupEntity> findAllByStaffId(Long storeId);

    RegularGroupEntity getById(Long id);

    void delete(RegularGroupEntity regularGroup);
}
