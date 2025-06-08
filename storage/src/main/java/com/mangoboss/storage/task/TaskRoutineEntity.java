package com.mangoboss.storage.task;

import com.mangoboss.storage.BaseTimeEntity;
import com.mangoboss.storage.task.converter.DayOfWeekListConverter;
import com.mangoboss.storage.task.converter.IntegerListConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskRoutineRepeatType repeatType;

    @Convert(converter = DayOfWeekListConverter.class)
    private List<DayOfWeek> repeatDays;

    @Convert(converter = IntegerListConverter.class)
    private List<Integer> repeatDates;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private boolean photoRequired;

    private String referenceImageUrl;

    @Builder
    private TaskRoutineEntity(
            final Long storeId,
            final String title,
            final String description,
            final TaskRoutineRepeatType repeatType,
            final List<DayOfWeek> repeatDays,
            final List<Integer> repeatDates,
            final LocalTime startTime,
            final LocalTime endTime,
            final LocalDate startDate,
            final LocalDate endDate,
            final boolean photoRequired,
            final String referenceImageUrl
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
        this.photoRequired = photoRequired;
        this.referenceImageUrl = referenceImageUrl;
    }

    public static TaskRoutineEntity createDaily(final Long storeId, final String title, final String description,
                                                final LocalDate startDate, final LocalDate endDate,
                                                final LocalTime startTime, final LocalTime endTime,
                                                final boolean photoRequired, final String referenceImageUrl) {
        return TaskRoutineEntity.builder()
                .storeId(storeId)
                .title(title)
                .description(description)
                .repeatType(TaskRoutineRepeatType.DAILY)
                .startDate(startDate)
                .endDate(endDate)
                .startTime(startTime)
                .endTime(endTime)
                .photoRequired(photoRequired)
                .referenceImageUrl(referenceImageUrl)
                .build();
    }

    public static TaskRoutineEntity createWeekly(
            final Long storeId,
            final String title,
            final String description,
            final List<DayOfWeek> repeatDays,
            final LocalDate startDate,
            final LocalDate endDate,
            final LocalTime startTime,
            final LocalTime endTime,
            final boolean photoRequired,
            final String referenceImageUrl
    ) {
        return TaskRoutineEntity.builder()
                .storeId(storeId)
                .title(title)
                .description(description)
                .repeatType(TaskRoutineRepeatType.WEEKLY)
                .repeatDays(repeatDays)
                .startDate(startDate)
                .endDate(endDate)
                .startTime(startTime)
                .endTime(endTime)
                .photoRequired(photoRequired)
                .referenceImageUrl(referenceImageUrl)
                .build();
    }

    public static TaskRoutineEntity createMonthly(
            final Long storeId,
            final String title,
            final String description,
            final List<Integer> repeatDates,
            final LocalDate startDate,
            final LocalDate endDate,
            final LocalTime startTime,
            final LocalTime endTime,
            final boolean photoRequired,
            final String referenceImageUrl
    ) {
        return TaskRoutineEntity.builder()
                .storeId(storeId)
                .title(title)
                .description(description)
                .repeatType(TaskRoutineRepeatType.MONTHLY)
                .repeatDates(repeatDates)
                .startDate(startDate)
                .endDate(endDate)
                .startTime(startTime)
                .endTime(endTime)
                .photoRequired(photoRequired)
                .referenceImageUrl(referenceImageUrl)
                .build();
    }


}

