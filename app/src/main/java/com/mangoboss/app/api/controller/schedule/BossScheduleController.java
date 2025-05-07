package com.mangoboss.app.api.controller.schedule;

import com.mangoboss.app.api.facade.schedule.BossScheduleFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.schedule.request.ScheduleCreateRequest;
import com.mangoboss.app.dto.schedule.request.ScheduleUpdateRequest;
import com.mangoboss.app.dto.schedule.response.ScheduleDailyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @DeleteMapping("/{scheduleId}")
    public void deleteSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable Long storeId, @PathVariable Long scheduleId) {
        final Long userId = userDetails.getUserId();
        bossScheduleFacade.deleteSchedule(storeId, userId, scheduleId);
    }

    @PutMapping("/{scheduleId}")
    public void updateSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @PathVariable Long storeId, @PathVariable Long scheduleId, @RequestBody @Valid ScheduleUpdateRequest request) {
        final Long userId = userDetails.getUserId();
        bossScheduleFacade.updateSchedule(storeId, scheduleId, userId, request);
    }
}
