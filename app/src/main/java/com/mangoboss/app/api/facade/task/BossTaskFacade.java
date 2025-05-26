package com.mangoboss.app.api.facade.task;

import com.mangoboss.app.common.constant.S3FileType;
import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.task.TaskService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.domain.service.task.context.TaskRoutineStrategyContext;
import com.mangoboss.app.domain.service.task.strategy.TaskRoutineGenerationStrategy;
import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.app.dto.task.request.SingleTaskCreateRequest;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import com.mangoboss.app.dto.task.request.TaskRoutineCreateRequest;
import com.mangoboss.app.dto.task.response.AssignedTaskResponse;
import com.mangoboss.app.dto.task.response.TaskLogDetailResponse;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskLogEntity;
import com.mangoboss.storage.task.TaskRoutineEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BossTaskFacade {

    private final TaskService taskService;
    private final StoreService storeService;
    private final StaffService staffService;
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

    public List<AssignedTaskResponse> getTasksByDate(final Long storeId, final Long bossId, final LocalDate date) {
        storeService.isBossOfStore(storeId, bossId);

        final List<TaskEntity> tasks = taskService.getTasksByDate(storeId, date);
        if (tasks.isEmpty()) {
            return List.of();
        }

        final List<Long> taskIds = tasks.stream().map(TaskEntity::getId).toList();
        final List<TaskLogEntity> taskLogs = taskService.getTaskLogsByTaskIds(taskIds);
        final Map<Long, TaskLogEntity> logMap = taskLogs.stream()
                .collect(Collectors.toMap(log -> log.getTask().getId(), log -> log));

        return tasks.stream()
                .map(task -> {
                    final TaskLogEntity log = logMap.get(task.getId());

                    final TaskLogDetailResponse taskLog = (log != null)
                            ? TaskLogDetailResponse.of(
                            log.getTaskLogImageUrl(),
                            log.getCreatedAt(),
                            StaffSimpleResponse.fromEntity(
                                    staffService.getStaffById(log.getStaffId())
                            )
                    )
                            : null;

                    return AssignedTaskResponse.of(task, taskLog);
                })
                .toList();
    }
}
