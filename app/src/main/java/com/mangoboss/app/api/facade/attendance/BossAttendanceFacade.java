package com.mangoboss.app.api.facade.attendance;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.attendance.response.AttendanceDetailResponse;
import com.mangoboss.storage.attendance.AttendanceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BossAttendanceFacade {
    private final StoreService storeService;
    private final AttendanceService attendanceService;

    public AttendanceDetailResponse getAttendanceDetail(final Long storeId, final Long bossId, final Long attendanceId) {
        storeService.isBossOfStore(storeId, bossId);
        final AttendanceEntity attendance = attendanceService.getAttendanceById(attendanceId);
        return AttendanceDetailResponse.fromEntity(attendance);
    }
}
