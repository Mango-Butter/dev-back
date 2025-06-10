package com.mangoboss.app.domain.service.task.strategy;

import com.mangoboss.app.dto.task.request.TaskRoutineCreateRequest;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskRoutineEntity;
import com.mangoboss.storage.task.TaskRoutineRepeatType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DailyTaskRoutineGenerationStrategyTest {

    private final DailyTaskRoutineGenerationStrategy strategy = new DailyTaskRoutineGenerationStrategy();

    @Test
    void DAILY_전략은_타입이_DAILY이다() {
        assertThat(strategy.getType()).isEqualTo(TaskRoutineRepeatType.DAILY);
    }

    @Test
    void 일일_루틴_생성시_반복규칙없이_정상_생성된다() {
        // given
        TaskRoutineCreateRequest request = new TaskRoutineCreateRequest(
                TaskRoutineRepeatType.DAILY,
                "매일 청소",
                "매일매일 해야지",
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                false,
                null,
                null
        );


        // when
        TaskRoutineEntity result = strategy.generateTaskRoutine(request, 1L);

        // then
        assertThat(result.getRepeatType()).isEqualTo(TaskRoutineRepeatType.DAILY);
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2025, 6, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2025, 6, 3));
    }

    @Test
    void 태스크루틴에서_일일_태스크를_생성할_수_있다() {
        // given
        TaskRoutineEntity routine = TaskRoutineEntity.createDaily(
                1L, "title", "desc",
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                false,
                null
        );

        // when
        List<TaskEntity> tasks = strategy.generateTasks(routine, 1L);

        // then
        assertThat(tasks).hasSize(3);
        assertThat(tasks.get(0).getTaskDate()).isEqualTo(LocalDate.of(2025, 6, 1));
        assertThat(tasks.get(2).getTaskDate()).isEqualTo(LocalDate.of(2025, 6, 3));
    }
}