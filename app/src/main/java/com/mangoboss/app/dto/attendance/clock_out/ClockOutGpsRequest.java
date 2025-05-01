package com.mangoboss.app.dto.attendance.clock_out;

import com.mangoboss.app.dto.attendance.base.ClockOutBaseRequest;
import com.mangoboss.app.dto.attendance.base.GpsRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ClockOutGpsRequest(
		@NotNull
		AttendanceMethod attendanceMethod,

		@NotNull
		Long attendanceId,

		@NotNull
		Double latitude,

		@NotNull
		Double longitude,

		@NotNull
		LocalDateTime locationFetchedAt
) implements ClockOutBaseRequest, GpsRequest {}
