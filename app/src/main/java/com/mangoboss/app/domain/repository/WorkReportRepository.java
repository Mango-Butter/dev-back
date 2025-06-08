package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.workreport.WorkReportEntity;
import com.mangoboss.storage.workreport.WorkReportTargetType;

import java.time.LocalDate;
import java.util.List;

public interface WorkReportRepository {
    WorkReportEntity save(WorkReportEntity entity);

    WorkReportEntity getByStoreIdAndWorkReportId(Long storeId, Long workReportId);

    List<WorkReportEntity> findByStoreIdAndDateOrderByCreatedAtDesc(Long storeId, LocalDate date);

    List<WorkReportEntity> findByStoreIdAndDateAndTargetTypeOrderByCreatedAtDesc(Long storeId, LocalDate date, WorkReportTargetType targetType);
}