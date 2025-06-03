package com.mangoboss.storage.store;

import com.mangoboss.storage.payroll.PayrollSettingEntity;
import com.mangoboss.storage.user.UserEntity;
import jakarta.persistence.*;

import com.mangoboss.storage.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    @Column(nullable = false, unique = true)
    private String inviteCode;

    //여기서부터 출퇴근 인증방식
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttendanceMethod attendanceMethod; // QR / GPS / BOTH

    private Integer gpsRangeMeters;

    @Column(nullable = false)
    private Double gpsLatitude;

    @Column(nullable = false)
    private Double gpsLongitude;

    @Column(nullable = false, unique = true)
    private String qrCode;

    @Column(nullable = false)
    private Integer overtimeLimit;

    @OneToOne(mappedBy = "store", fetch = FetchType.LAZY)
    private PayrollSettingEntity payrollSetting;

    @Builder
    private StoreEntity(final UserEntity boss, final String name, final String address, final String businessNumber,
                        final StoreType storeType, final String inviteCode, final AttendanceMethod attendanceMethod,
                        final Integer gpsRangeMeters, final Double gpsLatitude, final Double gpsLongitude, final String qrCode,
                        final Integer overtimeLimit) {
        this.boss = boss;
        this.name = name;
        this.address = address;
        this.businessNumber = businessNumber;
        this.storeType = storeType;
        this.inviteCode = inviteCode;
        this.attendanceMethod = attendanceMethod;
        this.gpsRangeMeters = gpsRangeMeters;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
        this.qrCode = qrCode;
        this.overtimeLimit = overtimeLimit;
    }

    public static StoreEntity create(final UserEntity boss, final String name, final String address,
                                     final String businessNumber, final StoreType storeType, final String inviteCode,
                                     final Double gpsLatitude, final Double gpsLongitude, final String qrCode) {
        return StoreEntity.builder()
                .boss(boss)
                .name(name)
                .address(address)
                .businessNumber(businessNumber)
                .storeType(storeType)
                .inviteCode(inviteCode)
                .attendanceMethod(AttendanceMethod.QR) // 기본값
                .gpsRangeMeters(50)
                .gpsLatitude(gpsLatitude)
                .gpsLongitude(gpsLongitude)
                .qrCode(qrCode)
                .overtimeLimit(0)
                .build();
    }

    public void updateInfo(final String address, final StoreType storeType, final Double gpsLatitude, final Double gpsLongitude, final Integer overtimeLimit) {
        this.address = address;
        this.storeType = storeType;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
        this.overtimeLimit = overtimeLimit;
    }

    public void updateInviteCode(final String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public StoreEntity updateAttendanceMethod(final AttendanceMethod method) {
        this.attendanceMethod = method;
        return this;
    }

    public void updateQrCode(final String newQrCode) {
        this.qrCode = newQrCode;
    }

    public StoreEntity updateGpsSettings(final String address, final Double gpsLatitude, final Double gpsLongitude, final Integer gpsRangeMeters) {
        this.address = address;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
        this.gpsRangeMeters = gpsRangeMeters;
        return this;
    }
}
