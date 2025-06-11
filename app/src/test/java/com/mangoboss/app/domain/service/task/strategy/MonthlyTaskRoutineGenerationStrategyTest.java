package com.mangoboss.app.domain.service.task.strategy;

import com.mangoboss.app.dto.task.request.RepeatRule;
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
class MonthlyTaskRoutineGenerationStrategyTest {

    private final MonthlyTaskRoutineGenerationStrategy strategy = new MonthlyTaskRoutineGenerationStrategy();

    @Test
    void MONTHLY_전략은_타입이_MONTHLY이다() {
        // then
        assertThat(strategy.getType()).isEqualTo(TaskRoutineRepeatType.MONTHLY);
    }

    @Test
    void MONTHLY_전략으로_루틴을_생성할_수_있다() {
        // given
        RepeatRule repeatRule = new RepeatRule(null, List.of(1, 10, 20));
        TaskRoutineCreateRequest request = new TaskRoutineCreateRequest(
                TaskRoutineRepeatType.MONTHLY,
                "월간 회의",
                "매달 회의 있음",
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 8, 31),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                false,
                null,
                repeatRule
        );

        // when
        TaskRoutineEntity routine = strategy.generateTaskRoutine(request, 1L);

        // then
        assertThat(routine.getRepeatDates()).containsExactly(1, 10, 20);
        assertThat(routine.getRepeatType()).isEqualTo(TaskRoutineRepeatType.MONTHLY);
    }

    @Test
    void 반복일자에_해당하는_날짜에만_태스크가_생성된다() {
        // given
        List<Integer> repeatDates = List.of(1, 10, 20);

        TaskRoutineEntity routine = TaskRoutineEntity.createMonthly(
                1L,
                "오전 회의",
                "정기 회의 루틴",
                repeatDates,
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 7, 31),
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                false,
                null
        );

        // when
        List<TaskEntity> tasks = strategy.generateTasks(routine, routine.getStoreId());

        // then
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).extracting(TaskEntity::getTaskDate)
                .allSatisfy(date -> assertThat(repeatDates).contains(date.getDayOfMonth()));
    }
}