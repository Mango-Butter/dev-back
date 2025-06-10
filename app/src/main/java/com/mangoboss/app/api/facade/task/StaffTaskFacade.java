package com.mangoboss.app.api.facade.task;

import com.mangoboss.app.common.util.S3PreSignedUrlManager;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.task.TaskService;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.app.dto.task.request.TaskCheckRequest;
import com.mangoboss.app.dto.task.response.AssignedTaskResponse;
import com.mangoboss.app.dto.task.response.TaskLogDetailResponse;
import com.mangoboss.storage.metadata.S3FileType;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskLogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffTaskFacade {

    private final TaskService taskService;
    private final StaffService staffService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    public void completeTask(final Long storeId, final Long taskId, final Long userId, final TaskCheckRequest request) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        taskService.completeTask(storeId, taskId, staff.getId(), request.reportImageUrl());
    }

    public void cancelCompleteTask(final Long storeId, final Long taskId, final Long userId) {
        staffService.getVerifiedStaff(userId, storeId);
        taskService.deleteTaskLog(storeId, taskId, userId);
    }

    public UploadPreSignedUrlResponse generateTaskReportImageUploadUrl(final Long storeId, final Long userId,
                                                                       final String extension, final String contentType) {
        staffService.getVerifiedStaff(userId, storeId);
        final String key = s3PreSignedUrlManager.generateFileKey(S3FileType.TASK_REPORT, extension);
        return s3PreSignedUrlManager.generateUploadPreSignedUrl(key, contentType);
    }

    public List<AssignedTaskResponse> getTasksByDate(final Long storeId, final Long userId, final LocalDate date) {
        staffService.getVerifiedStaff(userId, storeId);

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

    public AssignedTaskResponse getTaskDetail(final Long storeId, final Long userId, final Long taskId) {
        staffService.getVerifiedStaff(userId, storeId);

        final TaskEntity task = taskService.getTaskById(taskId);

        final TaskLogDetailResponse taskLogResponse = taskService.findTaskLogByTaskId(task.getId())
                .map(log -> TaskLogDetailResponse.of(
                        log.getTaskLogImageUrl(),
                        log.getCreatedAt(),
                        StaffSimpleResponse.fromEntity(staffService.getStaffById(log.getStaffId()))
                ))
                .orElse(null);

        return AssignedTaskResponse.of(task, taskLogResponse);
    }
}