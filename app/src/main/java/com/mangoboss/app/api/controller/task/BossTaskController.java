package com.mangoboss.app.api.controller.task;

import com.mangoboss.app.api.facade.task.BossTaskFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.task.request.SingleTaskCreateRequest;
import com.mangoboss.app.dto.task.request.TaskRoutineBaseRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boss/stores/{storeId}/tasks")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class BossTaskController {

    private final BossTaskFacade bossTaskFacade;

    @PostMapping("/task-routines")
    public void createTaskRoutine(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @PathVariable final Long storeId,
                                  @RequestBody @Valid TaskRoutineBaseRequest request) {
        final Long userId = userDetails.getUserId();
        bossTaskFacade.createTaskRoutine(userId, storeId, request);
    }

    @PostMapping
    public void createSingleTask(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @PathVariable final Long storeId,
                                 @RequestBody @Valid SingleTaskCreateRequest request) {
        final Long userId = userDetails.getUserId();
        bossTaskFacade.createSingleTask(userId, storeId, request);
    }
}