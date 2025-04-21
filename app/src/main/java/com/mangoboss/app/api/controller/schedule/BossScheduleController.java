package com.mangoboss.app.api.controller.schedule;

import com.mangoboss.app.api.facade.schedule.BossScheduleFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.schedule.request.ScheduleCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/boss/stores/{storeId}/schedules")
@PreAuthorize("hasRole('BOSS')")
public class BossScheduleController {

    private final BossScheduleFacade bossScheduleFacade;

    @PostMapping
    public void createSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable Long storeId, @RequestBody @Valid ScheduleCreateRequest request) {
        final Long userId = userDetails.getUserId();
        bossScheduleFacade.createSchedule(storeId, userId, request);
    }
}
