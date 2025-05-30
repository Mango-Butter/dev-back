package com.mangoboss.app.api.facade.workreport;

import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.domain.service.workreport.WorkReportService;
import com.mangoboss.app.dto.workreport.response.WorkReportResponse;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.workreport.WorkReportEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BossWorkReportFacade {

    private final WorkReportService workReportService;
    private final StoreService storeService;
    private final StaffService staffService;

    public List<WorkReportResponse> getWorkReportsByDate(final Long storeId, final Long bossId, final LocalDate date) {
        storeService.isBossOfStore(storeId, bossId);
        return workReportService.findByStoreIdAndDateOrderByCreatedAtDesc(storeId, date).stream()
                .map(workReport -> {
                    StaffEntity staff = staffService.getStaffById(workReport.getStaffId());
                    return WorkReportResponse.fromEntity(workReport, staff);
                })
                .toList();
    }

    public WorkReportResponse getWorkReportDetail(final Long storeId, final Long bossId, final Long workReportId) {
        storeService.isBossOfStore(storeId, bossId);
        final WorkReportEntity workReport = workReportService.getWorkReportByStoreIdAndId(storeId, workReportId);
        final StaffEntity staffEntity = staffService.getStaffById(workReport.getStaffId());
        return WorkReportResponse.fromEntity(workReport, staffEntity);
    }
}
