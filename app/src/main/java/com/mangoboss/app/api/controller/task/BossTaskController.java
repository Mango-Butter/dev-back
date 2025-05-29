package com.mangoboss.app.api.controller.task;

import com.mangoboss.app.api.facade.task.BossTaskFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.task.request.SingleTaskCreateRequest;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import com.mangoboss.app.dto.task.request.TaskRoutineCreateRequest;
import com.mangoboss.app.dto.task.response.AssignedTaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/boss/stores/{storeId}/tasks")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class BossTaskController {

    private final BossTaskFacade bossTaskFacade;

    @PostMapping("/task-routines")
    public void createTaskRoutine(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @PathVariable final Long storeId,
                                  @RequestBody @Valid TaskRoutineCreateRequest request) {
        final Long userId = userDetails.getUserId();
        bossTaskFacade.createTaskRoutine(storeId, userId, request);
    }

    @PostMapping
    public void createSingleTask(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @PathVariable final Long storeId,
                                 @RequestBody @Valid SingleTaskCreateRequest request) {
        final Long userId = userDetails.getUserId();
        bossTaskFacade.createSingleTask(storeId, userId, request);
    }

    @GetMapping("/reference-image/upload-url")
    public UploadPreSignedUrlResponse generateUploadUrl(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable final Long storeId,
                                                        @RequestParam final String extension,
                                                        @RequestParam final String contentType) {
        final Long userId = userDetails.getUserId();
        return bossTaskFacade.generateReferenceImageUploadUrl(storeId, userId, extension, contentType);
    }

    @GetMapping
    public ListWrapperResponse<AssignedTaskResponse> getTasksByDate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @PathVariable final Long storeId,
                                                                    @RequestParam final LocalDate date) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossTaskFacade.getTasksByDate(storeId, userId, date));
    }

    @GetMapping("/{taskId}")
    public AssignedTaskResponse getTaskDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable final Long storeId,
                                              @PathVariable final Long taskId) {
        final Long userId = userDetails.getUserId();
        return bossTaskFacade.getTaskDetail(storeId, userId, taskId);
    }

    @DeleteMapping("/{taskId}")
    public void deleteSingleTask(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @PathVariable final Long storeId,
                                 @PathVariable final Long taskId) {
        final Long userId = userDetails.getUserId();
        bossTaskFacade.deleteSingleTask(storeId, userId, taskId);
    }
}