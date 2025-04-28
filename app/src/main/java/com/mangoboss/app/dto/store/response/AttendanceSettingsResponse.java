package com.mangoboss.app.dto.store.response;

import com.mangoboss.storage.store.AttendanceMethod;
import lombok.Builder;

@Builder
public record AttendanceSettingsResponse(
		AttendanceMethod attendanceMethod
) {
	public static AttendanceSettingsResponse of(AttendanceMethod attendanceMethod) {
		return AttendanceSettingsResponse.builder()
				.attendanceMethod(attendanceMethod)
				.build();
	}
}