package com.mangoboss.app.dto.attendance.clock_out;

import com.mangoboss.app.dto.attendance.base.ClockOutBaseRequest;
import com.mangoboss.app.dto.attendance.base.GpsRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ClockOutGpsRequest(
		@NotBlank
		String attendanceMethod,

		@NotNull
		Long attendanceId,

		@NotNull
		Double latitude,

		@NotNull
		Double longitude,

		@NotNull
		LocalDateTime locationFetchedAt
) implements ClockOutBaseRequest, GpsRequest {}
