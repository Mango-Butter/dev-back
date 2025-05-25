package com.mangoboss.batch.clock_out;

import com.mangoboss.storage.schedule.ScheduleEntity;

import java.util.List;

public interface ScheduleRepository {
    List<ScheduleEntity> findAllSchedulesWithoutClockOut();

}
