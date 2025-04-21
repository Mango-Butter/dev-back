package com.mangoboss.app.infra.persistence;


import com.mangoboss.app.domain.repository.ScheduleRepository;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.schedule.ScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public ScheduleEntity save(final ScheduleEntity schedule) {
        return scheduleJpaRepository.save(schedule);
    }
}
