package com.mangoboss.storage.schedule.projection;

import com.mangoboss.storage.schedule.ScheduleEntity;

public interface ScheduleForNotificationProjection {
    ScheduleEntity getSchedule();

    String getStaffName();

    Long getBossId();

    Long getStoreId();
}
