package com.mangoboss.storage.task;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "task")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_routine_id")
    private TaskRoutineEntity taskRoutine;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean photoRequired;

    private String referenceImageUrl;

    @Column(nullable = false)
    private LocalDate taskDate;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Builder
    public TaskEntity(
            TaskRoutineEntity taskRoutine,
            Long storeId,
            LocalDate taskDate,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String title,
            String description,
            boolean photoRequired,
            String referenceImageUrl
    ) {
        this.taskRoutine = taskRoutine;
        this.storeId = storeId;
        this.taskDate = taskDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
        this.photoRequired = photoRequired;
        this.referenceImageUrl = referenceImageUrl;
    }

    public static TaskEntity createFromTaskRoutine(
            final TaskRoutineEntity routine,
            final Long storeId,
            final LocalDate taskDate,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    ) {
        return TaskEntity.builder()
                .taskRoutine(routine)
                .storeId(storeId)
                .taskDate(taskDate)
                .startTime(startTime)
                .endTime(endTime)
                .title(routine.getTitle())
                .description(routine.getDescription())
                .photoRequired(routine.isPhotoRequired())
                .referenceImageUrl(routine.getReferenceImageUrl())
                .build();
    }

    public static TaskEntity create(
            final Long storeId,
            final LocalDate taskDate,
            final LocalDateTime startTime,
            final LocalDateTime endTime,
            final String title,
            final String description,
            final boolean photoRequired,
            final String referenceImageUrl
    ) {
        return TaskEntity.builder()
                .storeId(storeId)
                .taskDate(taskDate)
                .startTime(startTime)
                .endTime(endTime)
                .title(title)
                .description(description)
                .photoRequired(photoRequired)
                .referenceImageUrl(referenceImageUrl)
                .build();
    }
}