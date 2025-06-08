package com.mangoboss.app.api.controller.schedule;

import com.mangoboss.app.api.facade.schedule.StaffScheduleFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.schedule.request.SubstituteRequestRequest;
import com.mangoboss.app.dto.schedule.response.SubstituteRequestResponse;
import com.mangoboss.app.dto.staff.response.SubstituteCandidateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/staff/stores/{storeId}/schedules")
@PreAuthorize("hasRole('STAFF')")
public class StaffScheduleController {
    private final StaffScheduleFacade staffScheduleFacade;

    @GetMapping("/{scheduleId}/substitute-candidates")
    public ListWrapperResponse<SubstituteCandidateResponse> getSubstituteCandidates(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long storeId, @PathVariable Long scheduleId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(staffScheduleFacade.getSubstituteCandidates(storeId, userId, scheduleId));
    }

    @PostMapping("/{scheduleId}/substitutions")
    public void requestSubstitution(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long storeId, @PathVariable Long scheduleId,
            @RequestBody @Valid SubstituteRequestRequest request) {
        final Long userId = userDetails.getUserId();
        staffScheduleFacade.requestSubstitution(storeId, userId, scheduleId, request);
    }

    @GetMapping("/substitutions")
    public ListWrapperResponse<SubstituteRequestResponse> getSubstituteRequests(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(staffScheduleFacade.getSubstituteRequests(storeId, userId));
    }
}
