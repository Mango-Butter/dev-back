package com.mangoboss.app.domain.service.task.context;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.service.task.strategy.TaskRoutineGenerationStrategy;
import com.mangoboss.storage.task.TaskRoutineRepeatType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class TaskRoutineStrategyContextTest {

    @Test
    void 주어진_타입에_해당하는_전략을_찾을_수_있다() {
        // given
        TaskRoutineGenerationStrategy dailyStrategy = mock(TaskRoutineGenerationStrategy.class);
        when(dailyStrategy.getType()).thenReturn(TaskRoutineRepeatType.DAILY);

        TaskRoutineStrategyContext context = new TaskRoutineStrategyContext(List.of(dailyStrategy));

        // when
        TaskRoutineGenerationStrategy result = context.findStrategy(TaskRoutineRepeatType.DAILY);

        // then
        assertThat(result).isEqualTo(dailyStrategy);
    }

    @Test
    void 해당_타입의_전략이_없으면_예외가_발생한다() {
        // given
        TaskRoutineGenerationStrategy weeklyStrategy = mock(TaskRoutineGenerationStrategy.class);
        when(weeklyStrategy.getType()).thenReturn(TaskRoutineRepeatType.WEEKLY);

        TaskRoutineStrategyContext context = new TaskRoutineStrategyContext(List.of(weeklyStrategy));

        // when & then
        assertThatThrownBy(() -> context.findStrategy(TaskRoutineRepeatType.MONTHLY))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.TASK_ROUTINE_STRATEGY_NOT_FOUND.getMessage());
    }
}
