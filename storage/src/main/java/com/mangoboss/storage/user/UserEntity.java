package com.mangoboss.storage.user;

import com.mangoboss.storage.BaseTimeEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.subscription.SubscriptionEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "\"user\"")
public class UserEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private Long kakaoId;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = true)
    private SubscriptionEntity subscription;

    @Builder
    private UserEntity(final Long kakaoId, final String name, final String email, final String phone,
                       final LocalDate birth, final String profileImageUrl, final Role role, final SubscriptionEntity subscription
    ) {
        this.kakaoId = kakaoId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birth = birth;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.subscription = subscription;
    }

    public static UserEntity create(final Long kakaoId, final String name, final String email, final String phone,
                                    final LocalDate birth, final String profileImageUrl, final Role role) {
        return UserEntity.builder()
                .kakaoId(kakaoId)
                .name(name)
                .email(email)
                .phone(phone)
                .birth(birth)
                .profileImageUrl(profileImageUrl)
                .role(role)
                .subscription(null)
                .build();
    }

    public void assignRole(final Role newRole) {
        this.role = newRole;
    }

    public void addSubscription(final SubscriptionEntity subscription) {
        this.subscription = subscription;
    }
}