package com.mangoboss.app.infra.persistence;


import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.ScheduleRepository;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.schedule.ScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public ScheduleEntity save(final ScheduleEntity schedule) {
        return scheduleJpaRepository.save(schedule);
    }

    @Override
    public List<ScheduleEntity> findAllByStoreIdAndWorkDate(final Long storeId, final LocalDate date){
        return scheduleJpaRepository.findAllByStoreIdAndWorkDate(storeId, date);
    }

    @Override
    public ScheduleEntity getScheduleById(final Long scheduleId) {
        return scheduleJpaRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.SCHEDULE_NOT_FOUND));
    }
}
