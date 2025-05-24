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
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task_routine")
public class TaskRoutineEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_routine_id")
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskRoutineRepeatType repeatType;

    @Column(columnDefinition = "json")
    private String repeatDays;

    @Column(columnDefinition = "json")
    private String repeatDates;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;


    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskLogVerificationType verificationType;

    private String referenceImageFileKey;

    @Builder
    private TaskRoutineEntity(
            final Long storeId,
            final String title,
            final String description,
            final TaskRoutineRepeatType repeatType,
            final String repeatDays,
            final String repeatDates,
            final LocalTime startTime,
            final LocalTime endTime,
            final LocalDate startDate,
            final LocalDate endDate,
            final TaskLogVerificationType verificationType,
            final String referenceImageFileKey
    ) {
        this.storeId = storeId;
        this.title = title;
        this.description = description;
        this.repeatType = repeatType;
        this.repeatDays = repeatDays;
        this.repeatDates = repeatDates;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.verificationType = verificationType;
        this.referenceImageFileKey = referenceImageFileKey;
    }

    public static TaskRoutineEntity createDaily(final Long storeId, final String title, final String description,
                                                final LocalDate startDate, final LocalDate endDate,
                                                final LocalTime startTime, final LocalTime endTime,
                                                final TaskLogVerificationType verificationType, final String referenceImageFileKey) {
        return TaskRoutineEntity.builder()
                .storeId(storeId)
                .title(title)
                .description(description)
                .repeatType(TaskRoutineRepeatType.DAILY)
                .startDate(startDate)
                .endDate(endDate)
                .startTime(startTime)
                .endTime(endTime)
                .verificationType(verificationType)
                .referenceImageFileKey(referenceImageFileKey)
                .build();
    }

    public static TaskRoutineEntity createWeekly(final Long storeId, final String title, final String description, final String repeatDaysJson,
                                                 final LocalDate startDate, final LocalDate endDate,
                                                 final TaskLogVerificationType verificationType, final String referenceImageFileKey) {
        return TaskRoutineEntity.builder()
                .storeId(storeId)
                .title(title)
                .description(description)
                .repeatType(TaskRoutineRepeatType.WEEKLY)
                .repeatDays(repeatDaysJson)
                .startDate(startDate)
                .endDate(endDate)
                .verificationType(verificationType)
                .referenceImageFileKey(referenceImageFileKey)
                .build();
    }

    public static TaskRoutineEntity createMonthly(final Long storeId, final String title, final String description, final String repeatDatesJson,
                                                  final LocalDate startDate, final LocalDate endDate,
                                                  final TaskLogVerificationType verificationType, final String referenceImageFileKey) {
        return TaskRoutineEntity.builder()
                .storeId(storeId)
                .title(title)
                .description(description)
                .repeatType(TaskRoutineRepeatType.MONTHLY)
                .repeatDates(repeatDatesJson)
                .startDate(startDate)
                .endDate(endDate)
                .verificationType(verificationType)
                .referenceImageFileKey(referenceImageFileKey)
                .build();
    }
}

