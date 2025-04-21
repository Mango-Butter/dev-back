package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.schedule.RegularGroupEntity;

public interface RegularGroupRepository {

    RegularGroupEntity save(RegularGroupEntity repeatGroup);
}
