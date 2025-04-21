package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.schedule.ScheduleEntity;
import org.springframework.stereotype.Repository;

public interface ScheduleRepository {
    ScheduleEntity save(ScheduleEntity schedule);
}
