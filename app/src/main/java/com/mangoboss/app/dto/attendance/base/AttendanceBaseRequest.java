package com.mangoboss.app.dto.attendance.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mangoboss.app.dto.attendance.request.AttendanceBothRequest;
import com.mangoboss.app.dto.attendance.request.AttendanceGpsRequest;
import com.mangoboss.app.dto.attendance.request.AttendanceQrRequest;
import com.mangoboss.storage.store.AttendanceMethod;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "attendanceMethod",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AttendanceQrRequest.class, name = "QR"),
        @JsonSubTypes.Type(value = AttendanceGpsRequest.class, name = "GPS"),
        @JsonSubTypes.Type(value = AttendanceBothRequest.class, name = "BOTH"),
})
public interface AttendanceBaseRequest {
    AttendanceMethod attendanceMethod();
    Long scheduleId();
}