package com.mangoboss.storage.store;

import com.mangoboss.storage.user.UserEntity;
import jakarta.persistence.*;

import java.sql.Time;

import com.mangoboss.storage.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "store")
public class StoreEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boss_id")
    private UserEntity boss;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String businessNumber;

    @Column(nullable = false)
    private StoreType storeType;

    @Column(nullable = false, unique = true)
    private String inviteCode;

    private String chatLink;

    private Time workingTimeUnit;

    //여기서부터 출퇴근 인증방식
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceMethod attendanceMethod;

    private Integer gpsRangeMeters;

    @Column(nullable = false)
    private Double gpsLatitude;

    @Column(nullable = false)
    private Double gpsLongitude;

    @Column(nullable = false, unique = true)
    private String qrCode;

    @Builder
    private StoreEntity(final UserEntity boss, final String name, final String address, final String businessNumber,
                        final StoreType storeType, final String inviteCode, final String chatLink, final Time workingTimeUnit,
                        final AttendanceMethod attendanceMethod, final Integer gpsRangeMeters,
                        final Double gpsLatitude, final Double gpsLongitude, final String qrCode) {
        this.boss = boss;
        this.name = name;
        this.address = address;
        this.businessNumber = businessNumber;
        this.storeType = storeType;
        this.inviteCode = inviteCode;
        this.chatLink = chatLink;
        this.workingTimeUnit = workingTimeUnit;
        this.attendanceMethod = attendanceMethod;
        this.gpsRangeMeters = gpsRangeMeters;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
        this.qrCode = qrCode;
    }

    public static StoreEntity create(final UserEntity boss, final String name, final String address,
            final String businessNumber, final StoreType storeType, final String inviteCode,
            final String chatLink, final Time workingTimeUnit, final Double gpsLatitude, final Double gpsLongitude, final String qrCode
    ) {
        return StoreEntity.builder()
                .boss(boss)
                .name(name)
                .address(address)
                .businessNumber(businessNumber)
                .storeType(storeType)
                .inviteCode(inviteCode)
                .chatLink(chatLink)
                .workingTimeUnit(workingTimeUnit)
                .attendanceMethod(AttendanceMethod.QR)
                .gpsRangeMeters(50)
                .gpsLatitude(gpsLatitude)
                .gpsLongitude(gpsLongitude)
                .qrCode(qrCode)
                .build();
    }
}
