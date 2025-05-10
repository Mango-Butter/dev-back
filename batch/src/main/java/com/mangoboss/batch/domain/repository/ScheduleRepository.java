package com.mangoboss.batch.domain.repository;

import com.mangoboss.storage.schedule.ScheduleEntity;

import java.util.List;

public interface ScheduleRepository {
    List<ScheduleEntity> findAllSchedulesWithoutClockOut();

}
