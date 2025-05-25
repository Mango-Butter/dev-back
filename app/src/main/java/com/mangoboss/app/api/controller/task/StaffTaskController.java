package com.mangoboss.app.api.controller.task;

import com.mangoboss.app.api.facade.task.StaffTaskFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import com.mangoboss.app.dto.task.request.TaskCheckRequest;
import com.mangoboss.app.dto.task.response.AssignedTaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/staff/stores/{storeId}/tasks")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffTaskController {

    private final StaffTaskFacade staffTaskFacade;

    @PostMapping("/{taskId}/complete")
    public void completeTask(@AuthenticationPrincipal CustomUserDetails userDetails,
                             @PathVariable final Long storeId,
                             @PathVariable final Long taskId,
                             @RequestBody @Valid TaskCheckRequest request) {
        final Long userId = userDetails.getUserId();
        staffTaskFacade.completeTask(storeId, taskId, userId, request);
    }

    @DeleteMapping("/{taskId}/completion")
    public void cancelCompleteTask(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   @PathVariable final Long storeId,
                                   @PathVariable final Long taskId) {
        final Long userId = userDetails.getUserId();
        staffTaskFacade.cancelCompleteTask(storeId, taskId, userId);
    }

    @GetMapping("/task-log-image/upload-url")
    public UploadPreSignedUrlResponse generateReportImageUploadUrl(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @PathVariable final Long storeId,
                                                                   @RequestParam final String extension,
                                                                   @RequestParam final String contentType) {
        final Long userId = userDetails.getUserId();
        return staffTaskFacade.generateReportImageUploadUrl(storeId, userId, extension, contentType);
    }

    @GetMapping
    public ListWrapperResponse<AssignedTaskResponse> getTasksByDate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @PathVariable final Long storeId,
                                                                    @RequestParam final LocalDate date) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(staffTaskFacade.getTasksByDate(storeId, userId, date));
    }
}
