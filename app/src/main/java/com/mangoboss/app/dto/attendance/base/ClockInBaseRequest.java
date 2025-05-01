package com.mangoboss.app.dto.attendance.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mangoboss.app.dto.attendance.clock_in.ClockInBothRequest;
import com.mangoboss.app.dto.attendance.clock_in.ClockInGpsRequest;
import com.mangoboss.app.dto.attendance.clock_in.ClockInQrRequest;
import com.mangoboss.storage.store.AttendanceMethod;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "attendanceMethod",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClockInQrRequest.class, name = "QR"),
        @JsonSubTypes.Type(value = ClockInGpsRequest.class, name = "GPS"),
        @JsonSubTypes.Type(value = ClockInBothRequest.class, name = "BOTH")
})
public interface ClockInBaseRequest {
    Long scheduleId();
    AttendanceMethod attendanceMethod();
}