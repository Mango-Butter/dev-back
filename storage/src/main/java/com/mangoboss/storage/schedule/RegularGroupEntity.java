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
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Builder
    private RegularGroupEntity(final DayOfWeek dayOfWeek, final LocalTime startTime, final LocalTime endTime,
                               final LocalDate startDate, final LocalDate endDate, final StaffEntity staff) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.staff = staff;
    }

    public static RegularGroupEntity create(final DayOfWeek dayOfWeek, final LocalTime startTime, final LocalTime endTime,
                                            final LocalDate startDate, final LocalDate endDate, final StaffEntity staff) {
        return RegularGroupEntity.builder()
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .startDate(startDate)
                .endDate(endDate)
                .staff(staff)
                .build();
    }
}
