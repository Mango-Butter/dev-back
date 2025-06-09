package com.mangoboss.batch.auto_clock_out.domain.service;

import com.mangoboss.batch.common.repository.AttendanceRepository;
import com.mangoboss.batch.common.repository.ScheduleRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.attendance.ClockOutStatus;
import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.schedule.projection.ScheduleForNotificationProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AutoClockOutService {
    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;
    private final NotificationAutoClockOutService notificationAutoClockOutService;

    @Transactional
    public void autoClockOut() {
        List<ScheduleForNotificationProjection> schedules = scheduleRepository.findAllSchedulesWithoutClockOut();
        List<AttendanceEntity> attendances = schedules.stream().map(projection -> {
            ScheduleEntity schedule = projection.getSchedule();
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

    private void notifyAbsentClockOut(final List<ScheduleForNotificationProjection> projections) {
        notificationAutoClockOutService.saveNotifications(projections);
    }
}
