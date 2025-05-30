package com.mangoboss.storage.workreport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkReportJpaRepository extends JpaRepository<WorkReportEntity, Long> {

    @Query("""
                SELECT wr FROM WorkReportEntity wr
                WHERE wr.storeId = :storeId AND wr.id = :workReportId
            """)
    Optional<WorkReportEntity> findByStoreIdAndWorkReportId(@Param("storeId") Long storeId, @Param("workReportId") Long workReportId);

    @Query("""
                SELECT wr FROM WorkReportEntity wr
                WHERE wr.storeId = :storeId
                AND DATE(wr.createdAt) = :date
                ORDER BY wr.createdAt DESC
            """)
    List<WorkReportEntity> findByStoreIdAndDateOrderByCreatedAtDesc(@Param("storeId") Long storeId, @Param("date") LocalDate date);
}