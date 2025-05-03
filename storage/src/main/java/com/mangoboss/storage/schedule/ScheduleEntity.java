package com.mangoboss.storage.schedule;

import com.mangoboss.storage.BaseTimeEntity;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.staff.StaffEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "schedule")
public class ScheduleEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private StaffEntity staff;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "regular_group_id")
    private RegularGroupEntity regularGroup;

    @Column(nullable = false)
    private LocalDate workDate;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Long storeId;

    @OneToOne(mappedBy = "schedule", fetch = FetchType.LAZY)
    private AttendanceEntity attendance;

    @Builder
    private ScheduleEntity(final LocalDate workDate, final LocalDateTime startTime, final LocalDateTime endTime,
                           final StaffEntity staff, final RegularGroupEntity regularGroup, final Long storeId) {
        this.workDate = workDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.staff = staff;
        this.regularGroup = regularGroup;
        this.storeId = storeId;
    }

    public static ScheduleEntity create(final LocalDate workDate, final LocalDateTime startTime, final LocalDateTime endTime,
                                        final StaffEntity staff, final RegularGroupEntity regularGroup, final Long storeId){
        return ScheduleEntity.builder()
                .workDate(workDate)
                .startTime(startTime)
                .endTime(endTime)
                .staff(staff)
                .regularGroup(regularGroup)
                .storeId(storeId)
                .build();
    }
}
