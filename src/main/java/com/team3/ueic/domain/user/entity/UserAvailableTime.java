package com.team3.ueic.domain.user.entity;

import com.team3.ueic.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_available_times")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAvailableTime extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AvailableTime availableTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public UserAvailableTime(AvailableTime availableTime) {
        this.availableTime = availableTime;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
