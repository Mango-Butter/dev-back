package com.mangoboss.storage.attendance.projection;

public interface StaffAttendanceCountProjection {
    Long getStaffId();
    Integer getNormalCount();
    Integer getLateCount();
    Integer getAbsentCount();
}