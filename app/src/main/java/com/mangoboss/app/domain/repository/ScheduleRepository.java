package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.schedule.ScheduleEntity;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository {
    ScheduleEntity save(ScheduleEntity schedule);

    List<ScheduleEntity> findAllByStoreIdAndWorkDate(Long storeId, LocalDate date);
    ScheduleEntity getScheduleById(Long scheduleId);
}
