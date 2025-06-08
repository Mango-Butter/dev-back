package com.mangoboss.storage.attendance.projection;

import java.time.LocalDate;

public interface WorkDotProjection {
    LocalDate getDate();
    Integer getNormalCount();
    Integer getLateCount();
    Integer getAbsentCount();
    Integer getPreScheduleCount();
}
