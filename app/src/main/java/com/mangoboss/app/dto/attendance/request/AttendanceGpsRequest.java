package com.mangoboss.app.dto.attendance.request;

import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import com.mangoboss.app.dto.attendance.base.GpsRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AttendanceGpsRequest(
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
) implements AttendanceBaseRequest, GpsRequest {}
