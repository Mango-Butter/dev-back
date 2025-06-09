package com.mangoboss.batch.common.repository;

import com.mangoboss.storage.schedule.projection.ScheduleForNotificationProjection;

import java.util.List;

public interface ScheduleRepository {
    List<ScheduleForNotificationProjection> findAllSchedulesWithoutClockOut();

    List<ScheduleForNotificationProjection> findAllSchedulesWithoutClockIn();
}
