package com.mangoboss.app.domain.service.schedule;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.RegularGroupRepository;
import com.mangoboss.app.domain.repository.ScheduleRepository;
import com.mangoboss.storage.schedule.RegularGroupEntity;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private RegularGroupRepository regularGroupRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Captor
    private ArgumentCaptor<ScheduleEntity> scheduleCaptor;

    @Test
    void 단순_스케줄을_만들_수_있다() {
        //given
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(scheduleRepository.save(any(ScheduleEntity.class))).thenReturn(schedule);

        //when
        scheduleService.createSchedule(schedule);

        //then
        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    void 스케줄의_시작시간과_끝시간의_유효성을_검사한다() {
        //given
        LocalTime startTime = LocalTime.of(11, 0, 0);
        LocalTime endTime = LocalTime.of(15, 0, 0);

        //when
        //then
        assertThatNoException().isThrownBy(() -> scheduleService.validateTimeOrder(startTime, endTime));
    }

    @Test
    void 스케줄_유효성_검사에서_시작시간이_끝시간_이후면_에러를_던진다() {
        //given
        LocalTime startTime = LocalTime.of(16, 30, 0);
        LocalTime endTime = LocalTime.of(16, 0, 0);

        //when
        //then
        assertThatThrownBy(() -> scheduleService.validateTimeOrder(startTime, endTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.INVALID_SCHEDULE_TIME.getMessage());
    }

    @Test
    void 스케줄_시작시간이_현재시간_이후면_스케줄을_삭제할_수_있다() {
        //given
        Long scheduleId = 1L;
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(scheduleRepository.getById(scheduleId)).thenReturn(schedule);
        when(schedule.getStartTime()).thenReturn(LocalDateTime.now().plusDays(2));

        //when
        scheduleService.deleteScheduleById(scheduleId);

        //then
        verify(scheduleRepository, times(1)).delete(schedule);
    }

    @Test
    void 스케줄_시작날짜가_현재시간_이전이면_에러를_던진다() {
        //given
        Long scheduleId = 1L;
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(scheduleRepository.getById(scheduleId)).thenReturn(schedule);
        when(schedule.getStartTime()).thenReturn(LocalDateTime.now().minusMinutes(1));

        //then
        assertThatThrownBy(() -> scheduleService.deleteScheduleById(scheduleId))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.SCHEDULE_ALREADY_STARTED_CANNOT_DELETE.getMessage());
    }

    @Test
    void 고정_근무를_만들고_고정_스케줄들을_만들_수_있다() {
        //given
        Long storeId = 1L;
        List<RegularGroupEntity> regularGroups = List.of(RegularGroupEntity.builder()
                .dayOfWeek(DayOfWeek.TUESDAY)
                .startDate(LocalDate.of(2025, 3, 1))
                .endDate(LocalDate.of(2025, 3, 31))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(11, 30))
                .staff(mock(StaffEntity.class))
                .build());
        when(regularGroupRepository.save(any(RegularGroupEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(scheduleRepository.save(any(ScheduleEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //when
        scheduleService.createRegularGroupAndSchedules(regularGroups, storeId);
        //then
        verify(scheduleRepository, atLeastOnce()).save(scheduleCaptor.capture());

        List<ScheduleEntity> savedSchedules = scheduleCaptor.getAllValues();
        assertThat(savedSchedules).hasSize(4);
        assertThat(savedSchedules.get(0).getWorkDate()).isEqualTo(LocalDate.of(2025, 3, 4));
        assertThat(savedSchedules.get(3).getWorkDate()).isEqualTo(LocalDate.of(2025, 3, 25));
    }

    @Test
    void 고정_근무_유효성_검사에서_시작날짜가_끝날짜_이후면_에러를_던진다() {
        //given
        LocalDate startDate = LocalDate.of(2025, 3, 31);
        LocalDate endDate = LocalDate.of(2025, 2, 28);
        LocalTime startTime = LocalTime.of(16, 30, 0);
        LocalTime endTime = LocalTime.of(16, 0, 0);

        //when
        //then
        assertThatThrownBy(() -> scheduleService.validateDateOrder(startDate, endDate, startTime, endTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.INVALID_REGULAR_DATE.getMessage());
    }

    @Test
    void 고정근무_시작날짜가_오늘_이후이면_고정근무_자체를_삭제한다() {
        //given
        Long groupId = 1L;
        RegularGroupEntity regularGroup = mock(RegularGroupEntity.class);
        when(regularGroup.getStartDate()).thenReturn(LocalDate.now().plusDays(2));
        when(regularGroupRepository.getById(groupId)).thenReturn(regularGroup);

        //when
        scheduleService.terminateRegularGroup(groupId);

        //then
        verify(scheduleRepository).deleteAllByRegularGroupIdAndWorkDateAfter(eq(groupId), any());
        verify(regularGroupRepository).delete(regularGroup);
        verify(regularGroup, never()).terminate(any());
    }

    @Test
    void 고정근무_시작날짜가_오늘_이전이면_고정근무_종료날짜가_내일이_된다() {
        //given
        Long groupId = 1L;
        RegularGroupEntity regularGroup = mock(RegularGroupEntity.class);
        when(regularGroup.getStartDate()).thenReturn(LocalDate.now().minusDays(2));
        when(regularGroupRepository.getById(groupId)).thenReturn(regularGroup);

        //when
        scheduleService.terminateRegularGroup(groupId);

        //then
        verify(scheduleRepository).deleteAllByRegularGroupIdAndWorkDateAfter(eq(groupId), any());
        verify(regularGroupRepository, never()).delete(regularGroup);
        verify(regularGroup).terminate(eq(LocalDate.now()));
    }
}