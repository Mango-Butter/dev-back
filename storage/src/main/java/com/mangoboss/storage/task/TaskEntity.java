package com.mangoboss.storage.task;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

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

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskLogVerificationType verificationType;

    private String referenceImageUrl;

    @Column(nullable = false)
    private LocalDate taskDate;

    private LocalTime startTime;

    private LocalTime endTime;

    @Builder
    public TaskEntity(
            TaskRoutineEntity taskRoutine,
            Long storeId,
            LocalDate taskDate,
            LocalTime startTime,
            LocalTime endTime,
            String title,
            String description,
            TaskLogVerificationType verificationType,
            String referenceImageUrl
    ) {
        this.taskRoutine = taskRoutine;
        this.storeId = storeId;
        this.taskDate = taskDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
        this.verificationType = verificationType;
        this.referenceImageUrl = referenceImageUrl;
    }

    public static TaskEntity createFromTaskRoutine(
            final TaskRoutineEntity routine,
            final Long storeId,
            final LocalDate taskDate,
            final LocalTime startTime,
            final LocalTime endTime
    ) {
        return TaskEntity.builder()
                .taskRoutine(routine)
                .storeId(storeId)
                .taskDate(taskDate)
                .startTime(startTime)
                .endTime(endTime)
                .title(routine.getTitle())
                .description(routine.getDescription())
                .verificationType(routine.getVerificationType())
                .referenceImageUrl(routine.getReferenceImageFileKey())
                .build();
    }

    public static TaskEntity create(
            final Long storeId,
            final LocalDate taskDate,
            final LocalTime startTime,
            final LocalTime endTime,
            final String title,
            final String description,
            final TaskLogVerificationType verificationType,
            final String referenceImageFileKey
    ) {
        return TaskEntity.builder()
                .storeId(storeId)
                .taskDate(taskDate)
                .startTime(startTime)
                .endTime(endTime)
                .title(title)
                .description(description)
                .verificationType(verificationType)
                .referenceImageUrl(referenceImageFileKey)
                .build();
    }
}