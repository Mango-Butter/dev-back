package com.mangoboss.batch.common.persistence;

import com.mangoboss.batch.common.repository.ScheduleRepository;
import com.mangoboss.storage.schedule.ScheduleJpaRepository;
import com.mangoboss.storage.schedule.projection.ScheduleForNotificationProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final Clock clock;

    @Override
    public List<ScheduleForNotificationProjection> findAllSchedulesWithoutClockOut() {
        LocalDateTime oneHourAgo = LocalDateTime.now(clock).minusHours(1);
        return scheduleJpaRepository.findAllSchedulesWithoutClockOut(oneHourAgo);
    }

    @Override
    public List<ScheduleForNotificationProjection> findAllSchedulesWithoutClockIn() {
        LocalDateTime temMinuteAgo = LocalDateTime.now(clock).minusMinutes(10);
        return scheduleJpaRepository.findLateSchedulesWithoutAlarm(temMinuteAgo);
    }
}
