package com.mangoboss.batch.late_clock_in.domain.service;

import com.mangoboss.batch.common.repository.ScheduleRepository;
import com.mangoboss.storage.schedule.projection.ScheduleForLateClockInProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LateClockInService {
    private final ScheduleRepository scheduleRepository;
    private final NotificationForLateClockInService notificationService;

    @Transactional
    public void notifyLateClockIn(){
        List<ScheduleForLateClockInProjection> lateSchedules = scheduleRepository.findAllSchedulesWithoutClockIn();
        notificationService.saveNotifications(lateSchedules);
    }

}
