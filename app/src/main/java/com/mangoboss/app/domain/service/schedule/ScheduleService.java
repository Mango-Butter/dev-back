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
@Transactional
@Slf4j
public class ScheduleService {
    private static final int SCHEDULE_CREATE_LIMIT_MINUTES = 30;
    private static final int MAX_SCHEDULE_DURATION_HOURS = 16;
    private final ScheduleRepository scheduleRepository;
    private final RegularGroupRepository regularGroupRepository;
    private final Clock clock;

    @Transactional(readOnly = true)
    public void validateTime(final LocalTime startTime, final LocalTime endTime) {
        final LocalDate BASE_DATE = LocalDate.of(2000, 1, 1);
        LocalDateTime start = LocalDateTime.of(BASE_DATE, startTime);
        LocalDateTime end = LocalDateTime.of(
                endTime.isAfter(startTime) ? BASE_DATE : BASE_DATE.plusDays(1),
                endTime
        );

        Duration duration = Duration.between(start, end);
        if (duration.isZero() || duration.toHours() > MAX_SCHEDULE_DURATION_HOURS-1) {
            throw new CustomException(CustomErrorInfo.INVALID_SCHEDULE_TIME);
        }
    }

    @Transactional(readOnly = true)
    public void validateDate(final LocalDate startDate, final LocalDate endDate,
                             final LocalTime startTime, final LocalTime endTime) {
        final LocalDate now = LocalDate.now(clock);
        if (startDate.isBefore(now.plusDays(1)) || startDate.isAfter(endDate)
                || endDate.isAfter(startDate.plusYears(1))) {
            throw new CustomException(CustomErrorInfo.INVALID_REGULAR_DATE);
        }
        validateTime(startTime, endTime);
    }

    @Transactional(readOnly = true)
    public void validateScheduleCreatable(final LocalDate workDate, final LocalTime startTime) {
        final LocalDateTime limitTime = LocalDateTime.now(clock).plusMinutes(SCHEDULE_CREATE_LIMIT_MINUTES);
        if (!LocalDateTime.of(workDate, startTime).isAfter(limitTime)) {
            throw new CustomException(CustomErrorInfo.SCHEDULE_CREATION_TIME_EXCEEDED);
        }
    }

    public void createRegularGroupAndSchedules(final List<RegularGroupEntity> regularGroups, final Long storeId) {
        regularGroups.forEach(regularGroup -> {
            regularGroupRepository.save(regularGroup);
            createRegularSchedules(regularGroup, storeId);
        });
    }

    private void createRegularSchedules(final RegularGroupEntity regularGroup, final Long storeId) {
        final DayOfWeek start = regularGroup.getStartDate().getDayOfWeek();
        final DayOfWeek target = regularGroup.getDayOfWeek();
        int daysToAdd = (target.getValue() - start.getValue() + 7) % 7;
        LocalDate current = regularGroup.getStartDate().plusDays(daysToAdd);

        while (!current.isAfter(regularGroup.getEndDate())) {
            ScheduleEntity schedule = ScheduleEntity.create(current, regularGroup.getStartTime(),
                    regularGroup.getEndTime(), regularGroup.getStaff(), regularGroup, storeId);

            scheduleRepository.save(schedule);
            current = current.plusWeeks(1);
        }
    }

    public ScheduleEntity createSchedule(final ScheduleEntity schedule) {
        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleEntity> getDailySchedules(final Long storeId, final LocalDate date) {
        return scheduleRepository.findAllByStoreIdAndWorkDate(storeId, date);
    }

    @Transactional(readOnly = true)
    public List<RegularGroupEntity> getRegularGroupsForStaff(final Long staffId) {
        return regularGroupRepository.findAllByStaffId(staffId);
    }

    public void deleteScheduleById(final Long scheduleId) {
        final ScheduleEntity schedule = scheduleRepository.getById(scheduleId);

        final LocalDateTime now = LocalDateTime.now(clock);
        if (now.isAfter(schedule.getStartTime())) {
            throw new CustomException(CustomErrorInfo.CANNOT_MODIFY_PAST_SCHEDULE);
        }
        scheduleRepository.delete(schedule);
    }

    public void terminateRegularGroup(final Long regularGroupId) {
        final RegularGroupEntity regularGroup = regularGroupRepository.getById(regularGroupId);
        LocalDate nowDate = LocalDate.now(clock);
        LocalDate tomorrow = nowDate.plusDays(1);

        scheduleRepository.deleteAllByRegularGroupIdAndWorkDateAfter(regularGroupId, tomorrow);
        if (regularGroup.getStartDate().isAfter(nowDate)) {
            regularGroupRepository.delete(regularGroup);
            return;
        }
        regularGroup.terminate(nowDate);
    }

    @Transactional(readOnly = true)
    public List<ScheduleEntity> getSchedulesByStaffIdAndDate(final Long storeId, final LocalDate date) {
        return scheduleRepository.findAllByStaffIdAndWorkDate(storeId, date);
    }

    public void updateSchedule(final Long scheduleId, final LocalDate workDate,
                               final LocalTime starTime, final LocalTime endTime) {
        final ScheduleEntity schedule = scheduleRepository.getById(scheduleId);

        final LocalDateTime now = LocalDateTime.now(clock);
        if (now.isAfter(schedule.getStartTime())) {
            throw new CustomException(CustomErrorInfo.CANNOT_MODIFY_PAST_SCHEDULE);
        }
        schedule.update(workDate, starTime, endTime);
    }

    @Transactional(readOnly = true)
    public ScheduleEntity getScheduleById(final Long scheduleId) {
        return scheduleRepository.getById(scheduleId);
    }

    @Transactional(readOnly = true)
    public List<DayOfWeek> getDayOfWeeksForRegularGroup(final Long staffId) {
        final List<RegularGroupEntity> regularGroups = getRegularGroupsForStaff(staffId);
        return regularGroups
                .stream()
                .map(RegularGroupEntity::getDayOfWeek)
                .collect(Collectors.toSet())
                .stream()
                .toList();
    }

    @Transactional(readOnly = true)
    public void validateScheduleBelongsToStaff(final Long scheduleId, final Long staffId) {
        final ScheduleEntity schedule = getScheduleById(scheduleId);
        if (!schedule.getStaff().getId().equals(staffId)) {
            throw new CustomException(CustomErrorInfo.SCHEDULE_NOT_BELONG_TO_STAFF);
        }
    }
}
