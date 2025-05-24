package com.mangoboss.app.api.facade.task;

import com.mangoboss.app.common.constant.S3FileType;
import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.service.task.TaskService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.task.request.SingleTaskCreateRequest;
import com.mangoboss.app.dto.task.request.TaskRoutineBaseRequest;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BossTaskFacade {

    private final TaskService taskService;
    private final StoreService storeService;
    private final S3FileManager s3FileManager;

    public void createTaskRoutine(final Long userId, final Long storeId, final TaskRoutineBaseRequest request) {
        storeService.isBossOfStore(storeId, userId);
        taskService.createTaskRoutineWithTasks(storeId, request);
    }

    public void createSingleTask(final Long userId, final Long storeId, final SingleTaskCreateRequest request) {
        storeService.isBossOfStore(storeId, userId);
        taskService.createSingleTask(storeId, request);
    }

    public UploadPreSignedUrlResponse generateReferenceImageUploadUrl(final Long bossId, final Long storeId,
                                                                      final String extension, final String contentType) {
        storeService.isBossOfStore(storeId, bossId);
        final String key = s3FileManager.generateFileKey(S3FileType.TASK, extension);
        return s3FileManager.generateUploadPreSignedUrl(key, contentType);
    }
}
