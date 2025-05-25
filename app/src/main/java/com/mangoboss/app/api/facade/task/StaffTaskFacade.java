package com.mangoboss.app.api.facade.task;

import com.mangoboss.app.common.constant.S3FileType;
import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.task.TaskService;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import com.mangoboss.app.dto.task.request.TaskCheckRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffTaskFacade {

    private final TaskService taskService;
    private final StaffService staffService;
    private final S3FileManager s3FileManager;

    public void completeTask(final Long storeId, final Long taskId, final Long userId, final TaskCheckRequest request) {
        staffService.getVerifiedStaff(userId, storeId);
        taskService.completeTask(storeId, taskId, userId, request.reportImageUrl());
    }

    public UploadPreSignedUrlResponse generateReportImageUploadUrl(final Long storeId, final Long userId,
                                                                   final String extension, final String contentType) {
        staffService.getVerifiedStaff(userId, storeId);
        final String key = s3FileManager.generateFileKey(S3FileType.TASK_REPORT, extension);
        return s3FileManager.generateUploadPreSignedUrl(key, contentType);
    }
}