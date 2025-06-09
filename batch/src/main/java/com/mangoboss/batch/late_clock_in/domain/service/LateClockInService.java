package com.mangoboss.batch.late_clock_in.domain.service;

import com.mangoboss.batch.common.repository.ScheduleRepository;
import com.mangoboss.storage.schedule.projection.ScheduleForNotificationProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LateClockInService {
    private final ScheduleRepository scheduleRepository;
    private final NotificationLateClockInService notificationService;

    @Transactional
    public void notifyLateClockIn() {
        List<ScheduleForNotificationProjection> projections = scheduleRepository.findAllSchedulesWithoutClockIn();
        if (projections.isEmpty()) {
            return;
        }
        notificationService.saveNotifications(projections);
    }

}
