package com.mangoboss.batch.domain.service;

import com.mangoboss.batch.domain.repository.AttendanceRepository;
import com.mangoboss.batch.domain.repository.ScheduleRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.attendance.ClockOutStatus;
import com.mangoboss.storage.schedule.ScheduleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutoClockOutService {
    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;
    private final Clock clock;

    @Transactional
    public void autoClockOut() {
        List<ScheduleEntity> schedules = scheduleRepository.findAllSchedulesWithoutClockOut();
        List<AttendanceEntity> attendances = schedules.stream().map(schedule -> {
            if (schedule.getAttendance() == null) {
                return recordAbsentAttendance(schedule);
            }
            return recordNormalClockOutAttendance(schedule.getAttendance(), schedule.getEndTime());
        }).toList();
        attendanceRepository.saveAll(attendances);
    }

    private AttendanceEntity recordAbsentAttendance(final ScheduleEntity schedule) {
        return AttendanceEntity.create(null, null, ClockInStatus.ABSENT, ClockOutStatus.ABSENT, schedule);
    }

    private AttendanceEntity recordNormalClockOutAttendance(final AttendanceEntity attendance, final LocalDateTime clockOutTime) {
        return attendance.recordClockOut(clockOutTime, ClockOutStatus.NORMAL);
    }
}
