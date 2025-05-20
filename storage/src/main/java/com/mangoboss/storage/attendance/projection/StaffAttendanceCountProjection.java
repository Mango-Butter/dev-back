package com.mangoboss.storage.attendance.projection;

import com.mangoboss.storage.staff.StaffEntity;

public interface StaffAttendanceCountProjection {
    StaffEntity getStaff();
    Integer getNormalCount();
    Integer getLateCount();
    Integer getAbsentCount();
}