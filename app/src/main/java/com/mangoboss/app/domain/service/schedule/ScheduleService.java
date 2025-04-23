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

    public void createRegularGroupAndSchedules(final List<RegularGroupEntity> regularGroups) {
        regularGroups.forEach(regularGroup -> {
            regularGroupRepository.save(regularGroup);
            createRegularSchedules(regularGroup);
        });
    }

    private void createRegularSchedules(final RegularGroupEntity regularGroup) {
        LocalDate current = regularGroup.getStartDate();
        while (current.getDayOfWeek() != regularGroup.getDayOfWeek()) {
            current = current.plusDays(1);
        }
        while (!current.isAfter(regularGroup.getEndDate())) {
            ScheduleEntity schedule = ScheduleEntity.create(current, LocalDateTime.of(current, regularGroup.getStartTime()),
                    LocalDateTime.of(current, regularGroup.getEndTime()), regularGroup.getStaff(), regularGroup);

            scheduleRepository.save(schedule);
            current = current.plusWeeks(1);
        }
    }

    public void createSchedule(final ScheduleEntity schedule) {
        scheduleRepository.save(schedule);
    }


    @Transactional(readOnly = true)
    public List<ScheduleEntity> getDailySchedules(final Long storeId, final LocalDate date) {
        return scheduleRepository.findAllByStoreIdAndWorkDate(storeId, date);
    }

    @Transactional(readOnly = true)
    public List<RegularGroupEntity> getRegularGroupsForStaff(final Long staffId) {
        return regularGroupRepository.findAllByStaffId(staffId);
    }
}
