package com.mangoboss.app.dto.attendance.clock_in;

import com.mangoboss.app.dto.attendance.base.ClockInBaseRequest;
import com.mangoboss.app.dto.attendance.base.GpsRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ClockInGpsRequest(
		@NotNull
		AttendanceMethod attendanceMethod,

		@NotNull
		Long scheduleId,

		@NotNull
		Double latitude,

		@NotNull
		Double longitude,

		@NotNull
		LocalDateTime locationFetchedAt
) implements ClockInBaseRequest, GpsRequest {}
