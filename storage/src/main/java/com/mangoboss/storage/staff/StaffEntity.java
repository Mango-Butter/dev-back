package com.mangoboss.storage.staff;


import com.mangoboss.storage.BaseTimeEntity;
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

    @Column(nullable = false)
    private Integer hourlyWage;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String profileImageUrl;

    @Builder
    private StaffEntity(final String name, final String profileImageUrl, final UserEntity user, final StoreEntity store,
                        final Integer hourlyWage) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.user = user;
        this.store = store;
        this.hourlyWage = hourlyWage;
    }

    public static StaffEntity create(final UserEntity user, final StoreEntity store) {
        return StaffEntity.builder()
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .user(user)
                .store(store)
                .hourlyWage(10030)
                .build();
    }

    public void updateHourlyWage(final Integer newWage) {
        this.hourlyWage = newWage;
    }
}
