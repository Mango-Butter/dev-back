package com.mangoboss.storage.staff;


import com.mangoboss.storage.BaseTimeEntity;
import com.mangoboss.storage.payroll.BankCode;
import com.mangoboss.storage.payroll.WithholdingType;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "staff")
public class StaffEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WithholdingType withholdingType;

    @Column(nullable = false)
    private Integer hourlyWage;

    @Enumerated(EnumType.STRING)
    private BankCode bankCode;

    private String accountNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String profileImageUrl;

    @Builder
    private StaffEntity(final String name, final String profileImageUrl, final UserEntity user, final StoreEntity store,
                        final WithholdingType withholdingType, final Integer hourlyWage) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.user = user;
        this.store = store;
        this.withholdingType = withholdingType;
        this.hourlyWage = hourlyWage;
    }

    public static StaffEntity create(final UserEntity user, final StoreEntity store) {
        return StaffEntity.builder()
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .user(user)
                .store(store)
                .withholdingType(WithholdingType.NONE)
                .hourlyWage(10030)
                .build();
    }

    public void updateAccount(final BankCode bankCode, final String accountNumber) {
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
    }

    public void updateHourlyWage(final Integer hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public void updateWithholdingType(final WithholdingType withholdingType) {
        this.withholdingType = withholdingType;
    }

    public void registerAccount(final BankCode bankCode, final String accountNumber) {
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
    }
}
