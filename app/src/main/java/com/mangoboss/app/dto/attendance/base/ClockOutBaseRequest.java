package com.mangoboss.app.dto.attendance.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mangoboss.app.dto.attendance.clock_out.ClockOutBothRequest;
import com.mangoboss.app.dto.attendance.clock_out.ClockOutGpsRequest;
import com.mangoboss.app.dto.attendance.clock_out.ClockOutQrRequest;
import com.mangoboss.storage.store.AttendanceMethod;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "attendanceMethod",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClockOutQrRequest.class, name = "QR"),
        @JsonSubTypes.Type(value = ClockOutGpsRequest.class, name = "GPS"),
        @JsonSubTypes.Type(value = ClockOutBothRequest.class, name = "BOTH")
})
public interface ClockOutBaseRequest {
    Long attendanceId();
    AttendanceMethod attendanceMethod();
}