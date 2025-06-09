package com.mangoboss.storage.schedule.projection;

import com.mangoboss.storage.schedule.ScheduleEntity;

public interface ScheduleForLateClockInProjection {
    ScheduleEntity getSchedule();
    String getStaffName();
    Long getBossId();
    Long getStoreId();
}
