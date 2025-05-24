package com.mangoboss.app.api.facade.task;

import com.mangoboss.app.domain.service.task.TaskService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.task.request.TaskRoutineBaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BossTaskFacade {

    private final TaskService taskService;
    private final StoreService storeService;

    public void createTaskRoutine(final Long userId, final Long storeId, final TaskRoutineBaseRequest request) {
        storeService.isBossOfStore(storeId, userId);
        taskService.createTaskRoutineWithTasks(storeId, request);
    }
}
