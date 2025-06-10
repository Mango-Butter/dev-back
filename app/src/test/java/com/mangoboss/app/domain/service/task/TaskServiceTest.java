package com.mangoboss.app.domain.service.task;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.repository.TaskLogRepository;
import com.mangoboss.app.domain.repository.TaskRepository;
import com.mangoboss.app.domain.repository.TaskRoutineRepository;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskLogEntity;
import com.mangoboss.storage.task.TaskRoutineEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRoutineRepository taskRoutineRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskLogRepository taskLogRepository;

    @Mock
    private S3FileManager s3FileManager;

    @Test
    void 루틴과_태스크를_함께_저장할_수_있다() {
        //given
        final TaskRoutineEntity routine = mock(TaskRoutineEntity.class);
        final List<TaskEntity> tasks = List.of(mock(TaskEntity.class), mock(TaskEntity.class));

        //when
        taskService.saveTaskRoutineWithTasks(routine, tasks);

        //then
        verify(taskRoutineRepository).save(routine);
        verify(taskRepository).saveAll(tasks);
    }

    @Test
    void 태스크를_정상적으로_완료할_수_있다() {
        //given
        final Long storeId = 1L, taskId = 2L, staffId = 3L;
        final String imageUrl = "http://s3.com/image.jpg";
        final TaskEntity task = mock(TaskEntity.class);
        given(task.getId()).willReturn(taskId);

        given(taskRepository.getTaskByIdAndStoreId(taskId, storeId)).willReturn(task);
        given(taskLogRepository.findByTaskIdAndStaffId(taskId, staffId)).willReturn(Optional.empty());

        //when
        taskService.completeTask(storeId, taskId, staffId, imageUrl);

        //then
        verify(taskLogRepository).save(any(TaskLogEntity.class));
    }

    @Test
    void 이미_완료된_태스크라면_예외를_던진다() {
        //given
        final Long taskId = 1L, staffId = 2L;
        given(taskLogRepository.findByTaskIdAndStaffId(taskId, staffId))
                .willReturn(Optional.of(mock(TaskLogEntity.class)));

        //when & then
        assertThatThrownBy(() -> taskService.validateNotAlreadyCompleted(taskId, staffId))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.ALREADY_COMPLETED_TASK.getMessage());
    }

    @Test
    void 이미지가_필수인_태스크에서_이미지가_없으면_예외를_던진다() {
        //given
        final TaskEntity task = mock(TaskEntity.class);
        given(task.isPhotoRequired()).willReturn(true);

        //when & then
        assertThatThrownBy(() -> taskService.validateTaskLogImageRequirement(task, null))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.TASK_LOG_IMAGE_REQUIRED.getMessage());
    }

    @Test
    void 태스크_로그를_삭제할_수_있다() {
        //given
        final Long storeId = 1L, taskId = 2L, staffId = 3L;
        final TaskEntity task = mock(TaskEntity.class);
        final TaskLogEntity log = mock(TaskLogEntity.class);

        given(taskRepository.getTaskByIdAndStoreId(taskId, storeId)).willReturn(task);
        given(taskLogRepository.getTaskLogByTaskIdAndStaffId(task.getId(), staffId)).willReturn(log);
        given(log.getTaskLogImageUrl()).willReturn("http://s3.com/image.jpg");
        given(s3FileManager.extractKeyFromPublicUrl(any())).willReturn("image.jpg");

        //when
        taskService.deleteTaskLog(storeId, taskId, staffId);

        //then
        verify(s3FileManager).deleteFileFromPublicBucket("image.jpg");
        verify(taskLogRepository).delete(log);
    }

    @Test
    void 루틴삭제시_ALL옵션이라면_전체태스크와_루틴이_삭제된다() {
        //given
        final Long storeId = 1L, routineId = 2L;
        final TaskRoutineEntity routine = mock(TaskRoutineEntity.class);
        given(taskRoutineRepository.getByIdAndStoreId(routineId, storeId)).willReturn(routine);

        //when
        taskService.deleteTaskRoutine(storeId, routineId, "ALL");

        //then
        verify(taskRepository).deleteAllByTaskRoutineId(routineId);
        verify(taskRoutineRepository).delete(routine);
    }

    @Test
    void 루틴삭제시_PENDING옵션이라면_미완료태스크만_삭제된다() {
        //given
        final Long storeId = 1L, routineId = 2L;
        final TaskRoutineEntity routine = mock(TaskRoutineEntity.class);
        given(taskRoutineRepository.getByIdAndStoreId(routineId, storeId)).willReturn(routine);

        //when
        taskService.deleteTaskRoutine(storeId, routineId, "PENDING");

        //then
        verify(taskRepository).deleteAllByTaskRoutineIdAndNotCompleted(routineId);
        verify(taskRepository).deleteRoutineReferenceForCompletedTasks(routineId);
        verify(taskRoutineRepository).delete(routine);
    }

    @Test
    void 업무와_업무보고를_삭제할_수_있다() {
        // given
        final Long storeId = 1L;
        final Long taskId = 2L;
        final TaskEntity task = mock(TaskEntity.class);
        final TaskLogEntity log = mock(TaskLogEntity.class);

        given(taskRepository.getTaskByIdAndStoreId(taskId, storeId)).willReturn(task);
        given(taskLogRepository.findTaskLogByTaskId(taskId)).willReturn(Optional.of(log));

        // when
        taskService.deleteSingleTask(storeId, taskId);

        // then
        verify(taskLogRepository).delete(log);
        verify(taskRepository).delete(task);
    }

    @Test
    void 특정_날짜에_해당하는_업무들을_조회할_수_있다() {
        // given
        final Long storeId = 1L;
        final LocalDate date = LocalDate.now();
        final List<TaskEntity> tasks = List.of(mock(TaskEntity.class));

        given(taskRepository.findByStoreIdAndTaskDate(storeId, date)).willReturn(tasks);

        // when
        List<TaskEntity> result = taskService.getTasksByDate(storeId, date);

        // then
        assertThat(result).isEqualTo(tasks);
    }

    @Test
    void 단일_업무를_저장할_수_있다() {
        // given
        final TaskEntity task = mock(TaskEntity.class);

        // when
        taskService.saveSingleTask(task);

        // then
        verify(taskRepository).save(task);
    }

    @Test
    void 업무_리스트로_업무보고를_조회할_수_있다() {
        // given
        final List<Long> taskIds = List.of(1L, 2L);
        final List<TaskLogEntity> logs = List.of(mock(TaskLogEntity.class));

        given(taskLogRepository.findByTaskIds(taskIds)).willReturn(logs);

        // when
        List<TaskLogEntity> result = taskService.getTaskLogsByTaskIds(taskIds);

        // then
        assertThat(result).isEqualTo(logs);
    }

    @Test
    void 매장으로_루틴을_조회할_수_있다() {
        // given
        final Long storeId = 1L;
        final List<TaskRoutineEntity> routines = List.of(mock(TaskRoutineEntity.class));

        given(taskRoutineRepository.findAllByStoreId(storeId)).willReturn(routines);

        // when
        List<TaskRoutineEntity> result = taskService.getTaskRoutinesByStoreId(storeId);

        // then
        assertThat(result).isEqualTo(routines);
    }

    @Test
    void 특정_태스크를_상세조회할_수_있다() {
        // given
        final Long taskId = 1L;
        final TaskEntity task = mock(TaskEntity.class);

        given(taskRepository.getTaskById(taskId)).willReturn(task);

        // when
        TaskEntity result = taskService.getTaskById(taskId);

        // then
        assertThat(result).isEqualTo(task);
    }

    @Test
    void 특정_태스크로그를_상세조회할_수_있다() {
        // given
        final Long taskId = 1L;
        final TaskLogEntity log = mock(TaskLogEntity.class);
        given(taskLogRepository.findTaskLogByTaskId(taskId)).willReturn(Optional.of(log));

        // when
        Optional<TaskLogEntity> result = taskService.findTaskLogByTaskId(taskId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(log);
    }

    @Test
    void 이미지가_없으면_s3삭제를_수행하지_않는다() {
        // given
        final Long storeId = 1L, taskId = 2L, staffId = 3L;
        final TaskEntity task = mock(TaskEntity.class);
        final TaskLogEntity log = mock(TaskLogEntity.class);

        given(task.getId()).willReturn(taskId);
        given(taskRepository.getTaskByIdAndStoreId(taskId, storeId)).willReturn(task);
        given(taskLogRepository.getTaskLogByTaskIdAndStaffId(taskId, staffId)).willReturn(log);
        given(log.getTaskLogImageUrl()).willReturn("");

        // when
        taskService.deleteTaskLog(storeId, taskId, staffId);

        // then
        verify(s3FileManager, org.mockito.Mockito.never()).deleteFileFromPublicBucket(any());
        verify(taskLogRepository).delete(log);
    }

    @Test
    void 이미지가_공백일때_예외를_던진다() {
        // given
        final TaskEntity task = mock(TaskEntity.class);
        given(task.isPhotoRequired()).willReturn(true);

        // when & then
        assertThatThrownBy(() -> taskService.validateTaskLogImageRequirement(task, " "))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.TASK_LOG_IMAGE_REQUIRED.getMessage());
    }

    @Test
    void 업무루틴삭제시_잘못된_옵션값이면_예외를_던진다() {
        // given
        final Long storeId = 1L;
        final Long taskRoutineId = 2L;
        final String invalidOption = "INVALID";

        // when & then
        assertThatThrownBy(() ->
                taskService.deleteTaskRoutine(storeId, taskRoutineId, invalidOption)
        ).isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.INVALID_DELETE_OPTION.getMessage());
    }
}