package com.mangoboss.app.domain.service.workreport;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.WorkReportRepository;
import com.mangoboss.storage.workreport.WorkReportEntity;
import com.mangoboss.storage.workreport.WorkReportTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkReportService {

    private final WorkReportRepository workReportRepository;

    public List<WorkReportEntity> findByStoreIdAndDateOrderByCreatedAtDesc(final Long storeId, final LocalDate date) {
        return workReportRepository.findByStoreIdAndDateOrderByCreatedAtDesc(storeId, date);
    }

    public List<WorkReportEntity> findStaffWorkReports(final Long storeId, final LocalDate date) {
        return workReportRepository.findByStoreIdAndDateAndTargetTypeOrderByCreatedAtDesc(storeId, date, WorkReportTargetType.TO_STAFF);
    }

    public WorkReportEntity getWorkReportByStoreIdAndId(final Long storeId, final Long workReportId) {
        return workReportRepository.getByStoreIdAndWorkReportId(storeId, workReportId);
    }

    @Transactional
    public WorkReportEntity createWorkReport(final Long storeId, final Long staffId, final String content, final String reportImageUrl, final WorkReportTargetType targetType) {
        final WorkReportEntity entity = WorkReportEntity.create(storeId, staffId, content, reportImageUrl, targetType);
        return workReportRepository.save(entity);
    }

    public void validateStaffHasAccessToWorkReport(final Long storeId, final Long workReportId) {
        final WorkReportEntity workReport = workReportRepository.getByStoreIdAndWorkReportId(storeId, workReportId);

        if (workReport.getTargetType() == WorkReportTargetType.TO_BOSS) {
            throw new CustomException(CustomErrorInfo.WORK_REPORT_ACCESS_DENIED);
        }
    }
}