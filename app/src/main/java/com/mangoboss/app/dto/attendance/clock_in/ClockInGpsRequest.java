package com.mangoboss.app.dto.attendance.clock_in;

import com.mangoboss.app.dto.attendance.base.ClockInBaseRequest;
import com.mangoboss.app.dto.attendance.base.GpsRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ClockInGpsRequest(
		@NotBlank
		String attendanceMethod,

		@NotNull
		Long scheduleId,

		@NotNull
		Double latitude,

		@NotNull
		Double longitude,

		@NotNull
		LocalDateTime locationFetchedAt
) implements ClockInBaseRequest, GpsRequest {}
