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
import com.mangoboss.storage.schedule.SubstitutionState;
import com.mangoboss.storage.staff.StaffEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
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

    private final LocalDateTime fixedNow = LocalDateTime.of(2025, 1, 1, 9, 0);
    private final Clock fixedClock = Clock.fixed(fixedNow.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @BeforeEach
    void setUp() {
        scheduleService = new ScheduleService(scheduleRepository, regularGroupRepository, fixedClock);
    }

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
    void 현재시각으로부터_30분_후의_스케줄만_만들_수_있도록_유효성을_검사한다() {
        //given
        LocalDate workDate = fixedNow.toLocalDate();
        LocalTime startTime = fixedNow.toLocalTime().plusMinutes(31);

        //when
        //then
        assertThatNoException().isThrownBy(() -> scheduleService.validateScheduleCreatable(workDate, startTime));
    }

    @Test
    void 현재시각으로부터_30분_이내의_스케줄을_만들면_에러를_던진다() {
        //given
        LocalDate workDate = fixedNow.toLocalDate();
        LocalTime startTime = fixedNow.toLocalTime().plusMinutes(30);

        //when
        //then
        assertThatThrownBy(() -> scheduleService.validateScheduleCreatable(workDate, startTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.SCHEDULE_CREATION_TIME_EXCEEDED.getMessage());
    }

    @Test
    void 스케줄의_시작시간과_끝시간의_유효성을_검사한다() {
        //given
        LocalTime startTime = LocalTime.of(7, 0, 0);
        LocalTime endTime = LocalTime.of(22, 59, 0);

        //when
        //then
        assertThatNoException().isThrownBy(() -> scheduleService.validateTime(startTime, endTime));
    }

    @Test
    void 스케줄_시간_유효성_검사에서_16시간_이상의_스케줄을_만들면_에러가_발생한다() {
        //given
        LocalTime startTime = LocalTime.of(7, 0, 0);
        LocalTime endTime = LocalTime.of(23, 0, 0);

        //when
        //then
        assertThatThrownBy(() -> scheduleService.validateTime(startTime, endTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.INVALID_SCHEDULE_TIME.getMessage());
    }

    @Test
    void 스케줄_시작시간이_현재시간_이후면_스케줄을_삭제할_수_있다() {
        //given
        Long scheduleId = 1L;
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(scheduleRepository.getById(scheduleId)).thenReturn(schedule);
        when(schedule.getStartTime()).thenReturn(LocalDateTime.now(fixedClock).plusDays(2));
        when(schedule.isRequested()).thenReturn(false);

        //when
        scheduleService.deleteScheduleById(scheduleId);

        //then
        verify(scheduleRepository, times(1)).delete(schedule);
    }

    @Test
    void 삭제할_스케줄의_시작날짜가_현재시간_이전이면_에러를_던진다() {
        //given
        Long scheduleId = 1L;
        ScheduleEntity schedule = mock(ScheduleEntity.class);
        when(scheduleRepository.getById(scheduleId)).thenReturn(schedule);
        when(schedule.getStartTime()).thenReturn(LocalDateTime.now(fixedClock).minusMinutes(1));
        when(schedule.isRequested()).thenReturn(false);

        //then
        assertThatThrownBy(() -> scheduleService.deleteScheduleById(scheduleId))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.CANNOT_MODIFY_PAST_SCHEDULE.getMessage());
    }

    @Test
    void 대타요청_중인_스케줄을_삭제하면_에러를_던진다() {
        //given
        Long scheduleId = 1L;
        LocalDate workDate = fixedNow.toLocalDate();
        LocalDateTime startTime = fixedNow.plusMinutes(20);
        LocalDateTime endTime = fixedNow.plusHours(2);
        ScheduleEntity schedule = ScheduleEntity.builder()
                .workDate(workDate)
                .startTime(startTime)
                .endTime(endTime)
                .regularGroup(mock(RegularGroupEntity.class))
                .substitutionState(SubstitutionState.REQUESTED)
                .build();
        when(scheduleRepository.getById(scheduleId)).thenReturn(schedule);

        //then
        assertThatThrownBy(() -> scheduleService.deleteScheduleById(scheduleId))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.SUBSTITUTE_REQUESTED.getMessage());
    }

    @Test
    void 스케줄_수정을_하면_고정근무에서_제외된다() {
        //given
        Long scheduleId = 1L;
        LocalDate workDate = fixedNow.toLocalDate().plusDays(1);
        LocalDateTime startTime = fixedNow;
        LocalDateTime endTime = fixedNow.plusHours(2);
        ScheduleEntity schedule = ScheduleEntity.builder()
                .workDate(workDate)
                .startTime(startTime)
                .endTime(endTime)
                .regularGroup(mock(RegularGroupEntity.class))
                .substitutionState(SubstitutionState.NONE)
                .build();
        when(scheduleRepository.getById(scheduleId)).thenReturn(schedule);

        //when
        scheduleService.updateSchedule(scheduleId, workDate.plusDays(3), startTime.toLocalTime(), endTime.toLocalTime());
        //then
        assertThat(schedule.getRegularGroup()).isNull();
    }

    @Test
    void 이미_지난_스케줄을_수정하려고_하면_에러를_던진다() {
        //given
        Long scheduleId = 1L;
        LocalDate workDate = fixedNow.toLocalDate();
        LocalDateTime startTime = fixedNow.minusMinutes(1);
        LocalDateTime endTime = fixedNow.plusHours(2);
        ScheduleEntity schedule = ScheduleEntity.builder()
                .workDate(workDate)
                .startTime(startTime)
                .endTime(endTime)
                .regularGroup(mock(RegularGroupEntity.class))
                .substitutionState(SubstitutionState.NONE)
                .build();
        when(scheduleRepository.getById(scheduleId)).thenReturn(schedule);

        //when
        //then
        assertThatThrownBy(() -> scheduleService.updateSchedule(scheduleId, fixedNow.toLocalDate().plusDays(1), startTime.toLocalTime(), endTime.toLocalTime()))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.CANNOT_MODIFY_PAST_SCHEDULE.getMessage());
    }

    @Test
    void 대타요청_중인_스케줄을_수정하면_에러를_던진다() {
        //given
        Long scheduleId = 1L;
        LocalDate workDate = fixedNow.toLocalDate();
        LocalDateTime startTime = fixedNow.plusHours(1);
        LocalDateTime endTime = fixedNow.plusHours(3);
        ScheduleEntity schedule = ScheduleEntity.builder()
                .workDate(workDate)
                .startTime(startTime)
                .endTime(endTime)
                .regularGroup(mock(RegularGroupEntity.class))
                .substitutionState(SubstitutionState.REQUESTED)
                .build();
        when(scheduleRepository.getById(scheduleId)).thenReturn(schedule);

        //when
        //then
        assertThatThrownBy(() -> scheduleService.updateSchedule(scheduleId, fixedNow.toLocalDate().plusDays(1), startTime.toLocalTime(), endTime.toLocalTime()))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.SUBSTITUTE_REQUESTED.getMessage());
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
        LocalTime startTime = LocalTime.of(9, 0, 0);
        LocalTime endTime = LocalTime.of(15, 0, 0);


        //when
        //then
        assertThatThrownBy(() -> scheduleService.validateDate(startDate, endDate, startTime, endTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.INVALID_REGULAR_DATE.getMessage());
    }

    @Test
    void 고정_근무_유효성_검사에서_시작날짜가_내일이후라면_유효하다() {
        //given
        LocalDate startDate = fixedNow.toLocalDate().plusDays(1);
        LocalDate endDate = fixedNow.toLocalDate().plusDays(20);
        LocalTime startTime = LocalTime.of(9, 0, 0);
        LocalTime endTime = LocalTime.of(15, 0, 0);

        //when
        //then
        assertThatNoException().isThrownBy(() -> scheduleService.validateDate(startDate, endDate, startTime, endTime));
    }

    @Test
    void 고정_근무_유효성_검사에서_시작날짜가_오늘이전이면_에러를_던진다() {
        //given
        LocalDate startDate = fixedNow.toLocalDate();
        LocalDate endDate = fixedNow.toLocalDate().plusDays(20);
        LocalTime startTime = LocalTime.of(9, 0, 0);
        LocalTime endTime = LocalTime.of(15, 0, 0);

        //when
        //then
        assertThatThrownBy(() -> scheduleService.validateDate(startDate, endDate, startTime, endTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.INVALID_REGULAR_DATE.getMessage());
    }

    @Test
    void 고정_근무_유효성_검사에서_고정_근무_기간이_1년보다_적으면_유효하다() {
        //given
        LocalDate startDate = fixedNow.toLocalDate().plusDays(1);
        LocalDate endDate = startDate.plusYears(1);
        LocalTime startTime = LocalTime.of(9, 0, 0);
        LocalTime endTime = LocalTime.of(15, 0, 0);


        //when
        //then
        assertThatNoException().isThrownBy(() -> scheduleService.validateDate(startDate, endDate, startTime, endTime));
    }

    @Test
    void 고정_근무_유효성_검사에서_고정_근무_기간이_1년을_초과하면_에러를_던진다() {
        //given
        LocalDate startDate = fixedNow.toLocalDate().plusDays(1);
        LocalDate endDate = startDate.plusYears(1).plusDays(1);
        LocalTime startTime = LocalTime.of(9, 0, 0);
        LocalTime endTime = LocalTime.of(15, 0, 0);


        //when
        //then
        assertThatThrownBy(() -> scheduleService.validateDate(startDate, endDate, startTime, endTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.INVALID_REGULAR_DATE.getMessage());
    }

    @Test
    void 고정근무에_해당하는_스케줄이_없으면_고정근무_자체를_삭제한다() {
        //given
        Long groupId = 1L;
        RegularGroupEntity regularGroup = mock(RegularGroupEntity.class);
        when(scheduleRepository.existsByRegularGroupId(groupId)).thenReturn(false);
        when(regularGroupRepository.getById(groupId)).thenReturn(regularGroup);

        //when
        scheduleService.terminateRegularGroup(groupId);

        //then
        verify(scheduleRepository).deleteAllByRegularGroupIdAndWorkDateAfter(eq(groupId), any());
        verify(regularGroupRepository).delete(regularGroup);
        verify(regularGroup, never()).terminate(any());
    }

    @Test
    void 고정근무에_해당하는_스케줄이_있으면_고정근무는_endDate를_마감한다() {
        //given
        Long groupId = 1L;
        RegularGroupEntity regularGroup = mock(RegularGroupEntity.class);
        when(scheduleRepository.existsByRegularGroupId(groupId)).thenReturn(true);
        when(regularGroupRepository.getById(groupId)).thenReturn(regularGroup);

        //when
        scheduleService.terminateRegularGroup(groupId);

        //then
        verify(scheduleRepository).deleteAllByRegularGroupIdAndWorkDateAfter(eq(groupId), any());
        verify(regularGroupRepository, never()).delete(regularGroup);
        verify(regularGroup).terminate(eq(LocalDate.now(fixedClock)));
    }

}