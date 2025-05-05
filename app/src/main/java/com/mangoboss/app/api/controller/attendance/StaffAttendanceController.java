package com.mangoboss.app.api.controller.attendance;

import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.mangoboss.app.api.facade.attendance.StaffAttendanceFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/staff/stores/{storeId}/schedules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffAttendanceController {
	private final StaffAttendanceFacade attendanceFacade;

	@PostMapping("/attendance/clock-in")
	public void clockIn(@AuthenticationPrincipal CustomUserDetails userDetails,
								   @PathVariable Long storeId,
								   @RequestBody @Valid AttendanceBaseRequest request) {
		final Long userId = userDetails.getUserId();
		attendanceFacade.clockIn(userId, storeId, request);
	}

	@PostMapping("/attendance/clock-out")
	public void clockOut(@AuthenticationPrincipal CustomUserDetails userDetails,
						 @PathVariable Long storeId,
						 @RequestBody @Valid AttendanceBaseRequest request) {
		final Long userId = userDetails.getUserId();
		attendanceFacade.clockOut(userId, storeId, request);
	}
}
