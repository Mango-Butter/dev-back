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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final RegularGroupRepository regularGroupRepository;

    public void validateTimeOrder(final LocalTime startTime, final LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new CustomException(CustomErrorInfo.INVALID_SCHEDULE_TIME);
        }
    }

    public void validateDateOrder(final LocalDate startDate, final LocalDate endDate,
                                  final LocalTime startTime, final LocalTime endTime) {
        if (startDate.isAfter(endDate)) {
            throw new CustomException(CustomErrorInfo.INVALID_REGULAR_DATE);
        }
        validateTimeOrder(startTime, endTime);
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
            ScheduleEntity schedule = ScheduleEntity.create(current, LocalDateTime.of(current, regularGroup.getStartTime()),
                    LocalDateTime.of(current, regularGroup.getEndTime()), regularGroup.getStaff(), regularGroup, storeId);

            scheduleRepository.save(schedule);
            current = current.plusWeeks(1);
        }
    }

    public void createSchedule(final ScheduleEntity schedule) {
        scheduleRepository.save(schedule);
    }


    // todo 삭제해야 함
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
        if (schedule.getStartTime().isBefore(LocalDateTime.now())) {
            throw new CustomException(CustomErrorInfo.SCHEDULE_ALREADY_STARTED_CANNOT_DELETE);
        }
        scheduleRepository.delete(schedule);
    }

    public void terminateRegularGroup(final Long regularGroupId) {
        final RegularGroupEntity regularGroup = regularGroupRepository.getById(regularGroupId);
        LocalDate nowDate = LocalDate.now();
        LocalDate tomorrow = nowDate.plusDays(1);

        scheduleRepository.deleteAllByRegularGroupIdAndWorkDateAfter(regularGroupId, tomorrow);
        if (regularGroup.getStartDate().isAfter(nowDate)) {
            regularGroupRepository.delete(regularGroup);
            return;
        }
        regularGroup.terminate(nowDate);
    }
}
