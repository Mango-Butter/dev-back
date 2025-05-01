package com.mangoboss.app.api.controller.attendance;

import com.mangoboss.app.dto.attendance.base.ClockInBaseRequest;
import com.mangoboss.app.dto.attendance.base.ClockOutBaseRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.mangoboss.app.api.facade.attendance.AttendanceFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/staff/stores/{storeId}")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffAttendanceController {
	private final AttendanceFacade attendanceFacade;

	@PostMapping("/clock-in")
	public void clockIn(@AuthenticationPrincipal CustomUserDetails userDetails,
						@PathVariable Long storeId,
						@RequestBody @Valid ClockInBaseRequest request) {
		final Long userId = userDetails.getUserId();
		attendanceFacade.clockIn(userId, storeId, request);
	}

	@PostMapping("/clock-out")
	public void clockOut(@AuthenticationPrincipal CustomUserDetails userDetails,
						 @PathVariable Long storeId,
						 @RequestBody @Valid ClockOutBaseRequest request) {
		final Long userId = userDetails.getUserId();
		attendanceFacade.clockOut(userId, storeId, request);
	}
}
