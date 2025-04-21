package com.mangoboss.storage.schedule;

import com.mangoboss.storage.staff.StaffEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "regular_group")
public class RegularGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regular_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private StaffEntity staff;

    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private LocalDate repeatStartDate;

    @Column(nullable = false)
    private LocalDate repeatEndDate;

    @Builder
    private RegularGroupEntity(final DayOfWeek dayOfWeek, final LocalTime startTime, final LocalTime endTime,
                               final LocalDate repeatStartDate, final LocalDate repeatEndDate, final StaffEntity staff) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatStartDate = repeatStartDate;
        this.repeatEndDate = repeatEndDate;
        this.staff = staff;
    }

    public static RegularGroupEntity create(final DayOfWeek dayOfWeek, final LocalTime startTime, final LocalTime endTime,
                                            final LocalDate repeatStartDate, final LocalDate repeatEndDate, final StaffEntity staff) {
        return RegularGroupEntity.builder()
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .repeatStartDate(repeatStartDate)
                .repeatEndDate(repeatEndDate)
                .staff(staff)
                .build();
    }
}
