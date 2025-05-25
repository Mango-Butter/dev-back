package com.mangoboss.app.api.facade.task;

import com.mangoboss.app.common.constant.S3FileType;
import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.service.task.TaskService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.domain.service.task.context.TaskRoutineStrategyContext;
import com.mangoboss.app.domain.service.task.strategy.TaskRoutineGenerationStrategy;
import com.mangoboss.app.dto.task.request.SingleTaskCreateRequest;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import com.mangoboss.app.dto.task.request.TaskRoutineCreateRequest;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskRoutineEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BossTaskFacade {

    private final TaskService taskService;
    private final StoreService storeService;
    private final S3FileManager s3FileManager;
    private final TaskRoutineStrategyContext taskRoutineStrategyContext;

    public void createTaskRoutine(final Long storeId, final Long bossId, final TaskRoutineCreateRequest request) {
        storeService.isBossOfStore(storeId, bossId);

        final TaskRoutineGenerationStrategy strategy = taskRoutineStrategyContext.findStrategy(request.taskRoutineRepeatType());
        final TaskRoutineEntity taskRoutine = strategy.generateTaskRoutine(request, storeId);
        final List<TaskEntity> tasks = strategy.generateTasks(taskRoutine, storeId);

        taskService.saveTaskRoutineWithTasks(taskRoutine, tasks);
    }

    public void createSingleTask(final Long storeId, final Long bossId, final SingleTaskCreateRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        final TaskEntity task = request.toEntity(storeId);
        taskService.saveSingleTask(task);
    }

    public UploadPreSignedUrlResponse generateReferenceImageUploadUrl(final Long storeId, final Long bossId,
                                                                      final String extension, final String contentType) {
        storeService.isBossOfStore(storeId, bossId);
        final String key = s3FileManager.generateFileKey(S3FileType.TASK_REFERENCE, extension);
        return s3FileManager.generateUploadPreSignedUrl(key, contentType);
    }
}
