package com.mangoboss.batch.common.repository;

import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.schedule.projection.ScheduleForLateClockInProjection;

import java.util.List;

public interface ScheduleRepository {
    List<ScheduleEntity> findAllSchedulesWithoutClockOut();

    List<ScheduleForLateClockInProjection> findAllSchedulesWithoutClockIn();
}
