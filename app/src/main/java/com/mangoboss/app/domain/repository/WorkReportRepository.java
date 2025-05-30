package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.workreport.WorkReportEntity;

import java.time.LocalDate;
import java.util.List;

public interface WorkReportRepository {
    WorkReportEntity getByStoreIdAndWorkReportId(Long storeId, Long workReportId);

    List<WorkReportEntity> findByStoreIdAndDateOrderByCreatedAtDesc(Long storeId, LocalDate date);
}