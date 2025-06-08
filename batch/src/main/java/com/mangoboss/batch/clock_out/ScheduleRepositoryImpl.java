package com.mangoboss.batch.clock_out;

import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.schedule.ScheduleJpaRepository;
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
    public List<ScheduleEntity> findAllSchedulesWithoutClockOut() {
        LocalDateTime oneHourAgo = LocalDateTime.now(clock).minusHours(1);
        return scheduleJpaRepository.findAllSchedulesWithoutClockOut(oneHourAgo);
    }
}
