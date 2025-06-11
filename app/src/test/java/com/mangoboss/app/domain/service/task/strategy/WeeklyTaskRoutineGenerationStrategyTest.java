package com.mangoboss.app.domain.service.task.strategy;

import com.mangoboss.app.dto.task.request.RepeatRule;
import com.mangoboss.app.dto.task.request.TaskRoutineCreateRequest;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskRoutineEntity;
import com.mangoboss.storage.task.TaskRoutineRepeatType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WeeklyTaskRoutineGenerationStrategyTest {

    private final WeeklyTaskRoutineGenerationStrategy strategy = new WeeklyTaskRoutineGenerationStrategy();

    @Test
    void WEEKLY_전략은_타입이_WEEKLY이다() {
        // then
        assertThat(strategy.getType()).isEqualTo(TaskRoutineRepeatType.WEEKLY);
    }

    @Test
    void WEEKLY_전략으로_루틴을_생성할_수_있다() {
        // given
        RepeatRule repeatRule = new RepeatRule(List.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), null);
        TaskRoutineCreateRequest request = new TaskRoutineCreateRequest(
                TaskRoutineRepeatType.WEEKLY,
                "주간 청소",
                "정기 청소",
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 30),
                LocalTime.of(10, 0),
                LocalTime.of(16, 0),
                false,
                null,
                repeatRule
        );

        // when
        TaskRoutineEntity routine = strategy.generateTaskRoutine(request, 1L);

        // then
        assertThat(routine.getRepeatDays()).containsExactlyInAnyOrder(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);
        assertThat(routine.getRepeatType()).isEqualTo(TaskRoutineRepeatType.WEEKLY);
    }

    @Test
    void 반복요일에_해당하는_날짜에만_태스크가_생성된다() {
        // given
        Set<DayOfWeek> repeatDays = Set.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY);

        TaskRoutineEntity routine = TaskRoutineEntity.createWeekly(
                1L,
                "청소 루틴",
                "매장 청소 루틴 설명",
                List.copyOf(repeatDays),
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 14),
                LocalTime.of(10, 0),
                LocalTime.of(16, 0),
                false,
                null
        );

        // when
        List<TaskEntity> tasks = strategy.generateTasks(routine, routine.getStoreId());

        // then
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).extracting(TaskEntity::getTaskDate)
                .allSatisfy(date -> assertThat(repeatDays).contains(date.getDayOfWeek()));
    }
}