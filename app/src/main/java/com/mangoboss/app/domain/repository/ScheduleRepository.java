package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.schedule.ScheduleEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository {
    ScheduleEntity save(ScheduleEntity schedule);

    List<ScheduleEntity> findAllByStoreIdAndWorkDate(Long storeId, LocalDate date);

    void delete(ScheduleEntity schedule);

    ScheduleEntity getById(Long id);

    void deleteAllByRegularGroupIdAndWorkDateAfter(Long regularGroupId, LocalDate date);
}
