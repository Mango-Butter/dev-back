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
    public List<ScheduleEntity> findAllByStoreIdAndWorkDate(final Long storeId, final LocalDate date) {
        return scheduleJpaRepository.findAllByStoreIdAndWorkDate(storeId, date);
    }

    @Override
    public void delete(final ScheduleEntity schedule) {
        scheduleJpaRepository.delete(schedule);
    }

    @Override
    public ScheduleEntity getById(final Long id) {
        return scheduleJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.SCHEDULE_NOT_FOUND));
    }

    @Override
    public void deleteAllByRegularGroupIdAndWorkDateAfter(final Long regularGroupId, final LocalDate date) {
        scheduleJpaRepository.deleteAllByRegularGroupIdAndWorkDateAfter(regularGroupId, date);
    }

    @Override
    public ScheduleEntity getByIdAndStaffId(final Long scheduleId, final Long staffId) {
        return scheduleJpaRepository.findByIdAndStaffId(scheduleId, staffId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.SCHEDULE_NOT_BELONG_TO_STAFF));
    }

    @Override
    public ScheduleEntity getByIdAndAttendanceIsNotNull(final Long scheduleId) {
        return scheduleJpaRepository.findByIdAndAttendanceIsNotNull(scheduleId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.ATTENDANCE_NOT_FOUND));
    }

    @Override
    public List<ScheduleEntity> findAllByStaffIdAndWorkDate(final Long staffId, final LocalDate date) {
        return scheduleJpaRepository.findAllByStaffIdAndWorkDate(staffId, date);
    }

    @Override
    public void deleteById(Long scheduleId) {
        scheduleJpaRepository.deleteById(scheduleId);
    }
}
