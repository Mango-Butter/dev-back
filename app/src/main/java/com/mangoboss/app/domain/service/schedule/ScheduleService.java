package com.mangoboss.app.domain.service.schedule;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.RegularGroupRepository;
import com.mangoboss.app.domain.repository.ScheduleRepository;
import com.mangoboss.storage.schedule.RegularGroupEntity;
import com.mangoboss.storage.schedule.ScheduleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScheduleService {
    private static final int SCHEDULE_CREATE_LIMIT_MINUTES = 30;
    private static final int MAX_SCHEDULE_DURATION_HOURS = 16;
    private static final int DAYS_IN_WEEK = 7;
    private final ScheduleRepository scheduleRepository;
    private final RegularGroupRepository regularGroupRepository;
    private final Clock clock;

    public void validateTime(final LocalTime startTime, final LocalTime endTime) {
        final LocalDate BASE_DATE = LocalDate.of(2000, 1, 1);
        LocalDateTime start = LocalDateTime.of(BASE_DATE, startTime);
        LocalDateTime end = LocalDateTime.of(
                endTime.isAfter(startTime) ? BASE_DATE : BASE_DATE.plusDays(1),
                endTime
        );

        Duration duration = Duration.between(start, end);
        if (duration.isZero() || duration.toHours() > MAX_SCHEDULE_DURATION_HOURS - 1) {
            throw new CustomException(CustomErrorInfo.INVALID_SCHEDULE_TIME);
        }
    }

    public void validateDate(final LocalDate startDate, final LocalDate endDate,
                             final LocalTime startTime, final LocalTime endTime) {
        final LocalDate now = LocalDate.now(clock);
        if (startDate.isBefore(now.plusDays(1)) || startDate.isAfter(endDate)
                || endDate.isAfter(startDate.plusYears(1))) {
            throw new CustomException(CustomErrorInfo.INVALID_REGULAR_DATE);
        }
        validateTime(startTime, endTime);
    }

    public void validateScheduleCreatable(final LocalDate workDate, final LocalTime startTime) {
        final LocalDateTime limitTime = LocalDateTime.now(clock).plusMinutes(SCHEDULE_CREATE_LIMIT_MINUTES);
        if (!LocalDateTime.of(workDate, startTime).isAfter(limitTime)) {
            throw new CustomException(CustomErrorInfo.SCHEDULE_CREATION_TIME_EXCEEDED);
        }
    }

    @Transactional
    public void createRegularGroupAndSchedules(final List<RegularGroupEntity> regularGroups, final Long storeId) {
        regularGroups.forEach(regularGroup -> {
            regularGroupRepository.save(regularGroup);
            createRegularSchedules(regularGroup, storeId);
        });
    }

    private void createRegularSchedules(final RegularGroupEntity regularGroup, final Long storeId) {
        final DayOfWeek start = regularGroup.getStartDate().getDayOfWeek();
        final DayOfWeek target = regularGroup.getDayOfWeek();
        int daysToAdd = (target.getValue() - start.getValue() + DAYS_IN_WEEK) % DAYS_IN_WEEK;
        LocalDate current = regularGroup.getStartDate().plusDays(daysToAdd);

        while (!current.isAfter(regularGroup.getEndDate())) {
            ScheduleEntity schedule = ScheduleEntity.create(current, regularGroup.getStartTime(),
                    regularGroup.getEndTime(), regularGroup.getStaff(), regularGroup, storeId);

            scheduleRepository.save(schedule);
            current = current.plusWeeks(1);
        }
    }

    @Transactional
    public ScheduleEntity createSchedule(final ScheduleEntity schedule) {
        return scheduleRepository.save(schedule);
    }


    public List<ScheduleEntity> getDailySchedules(final Long storeId, final LocalDate date) {
        return scheduleRepository.findAllByStoreIdAndWorkDate(storeId, date);
    }


    public List<RegularGroupEntity> getRegularGroupsForStaff(final Long staffId) {
        final LocalDate today = LocalDate.now(clock);
        return regularGroupRepository.findActiveOrUpcomingByStaffId(staffId, today);
    }

    @Transactional
    public void deleteScheduleById(final Long scheduleId) {
        final ScheduleEntity schedule = scheduleRepository.getById(scheduleId);

        isUpdatable(schedule);
        scheduleRepository.delete(schedule);
    }

    @Transactional
    public void terminateRegularGroup(final Long regularGroupId) {
        final RegularGroupEntity regularGroup = regularGroupRepository.getById(regularGroupId);
        LocalDate today = LocalDate.now(clock);
        LocalDate tomorrow = today.plusDays(1);

        scheduleRepository.deleteAllByRegularGroupIdAndWorkDateAfter(regularGroupId, tomorrow);
        if (!scheduleRepository.existsByRegularGroupId(regularGroupId)) {
            regularGroupRepository.delete(regularGroup);
            return;
        }
        regularGroup.terminate(today);
    }


    public List<ScheduleEntity> getSchedulesByStaffIdAndDate(final Long storeId, final LocalDate date) {
        return scheduleRepository.findAllByStaffIdAndWorkDate(storeId, date);
    }

    @Transactional
    public void updateSchedule(final Long scheduleId, final LocalDate workDate,
                               final LocalTime starTime, final LocalTime endTime) {
        final ScheduleEntity schedule = scheduleRepository.getById(scheduleId);
        isUpdatable(schedule);
        schedule.update(workDate, starTime, endTime);
    }

    private void isUpdatable(final ScheduleEntity schedule) {
        if (!schedule.isUpdatable()) {
            throw new CustomException(CustomErrorInfo.SUBSTITUTE_REQUESTED);
        }
        final LocalDateTime now = LocalDateTime.now(clock);
        if (now.isAfter(schedule.getStartTime())) {
            throw new CustomException(CustomErrorInfo.CANNOT_MODIFY_PAST_SCHEDULE);
        }
    }

    public ScheduleEntity getScheduleById(final Long scheduleId) {
        return scheduleRepository.getById(scheduleId);
    }

    public List<DayOfWeek> getDayOfWeeksForRegularGroup(final Long staffId) {
        final List<RegularGroupEntity> regularGroups = getRegularGroupsForStaff(staffId);
        return regularGroups
                .stream()
                .map(RegularGroupEntity::getDayOfWeek)
                .collect(Collectors.toSet())
                .stream()
                .toList();
    }


    public ScheduleEntity validateScheduleBelongsToStaff(final Long scheduleId, final Long staffId) {
        final ScheduleEntity schedule = getScheduleById(scheduleId);
        if (!schedule.getStaff().getId().equals(staffId)) {
            throw new CustomException(CustomErrorInfo.SCHEDULE_NOT_BELONG_TO_STAFF);
        }
        return schedule;
    }

    public Boolean isSubstituteCandidate(final Long staffId, final ScheduleEntity schedule) {
        return scheduleRepository.existsOverlappingSchedule(
                staffId,
                schedule.getWorkDate(),
                schedule.getStartTime(),
                schedule.getEndTime());
    }
}
