package com.mangoboss.storage.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByStoreIdAndTaskDate(Long storeId, LocalDate taskDate);

    Optional<TaskEntity> findByIdAndStoreId(Long id, Long storeId);

    void deleteAllByTaskRoutineId(Long routineId);

    @Modifying
    @Query("""
                DELETE FROM TaskEntity t
                WHERE t.taskRoutine.id = :routineId
                AND NOT EXISTS (
                    SELECT 1 FROM TaskLogEntity l
                    WHERE l.task.id = t.id
                )
            """)
    void deleteAllByTaskRoutineIdAndNotCompleted(@Param("routineId") Long routineId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE TaskEntity t
                    SET t.taskRoutine = null
                    WHERE t.taskRoutine.id = :routineId
                    AND EXISTS (
                        SELECT 1 FROM TaskLogEntity l
                        WHERE l.task.id = t.id
                    )
            """)
    void clearRoutineReferenceForCompletedTasks(@Param("routineId") Long routineId);
}
