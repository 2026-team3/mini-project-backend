package com.team3.ueic.domain.user.entity;

import com.team3.ueic.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(length = 512)
    private String refreshToken;

    private LocalDateTime refreshTokenExpiredAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAvailableTime> availableTimes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStudyStyleTag> studyStyleTags = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        userProfile.setUser(this);
    }

    public void addAvailableTime(UserAvailableTime availableTime) {
        this.availableTimes.add(availableTime);
        availableTime.setUser(this);
    }

    public void addStudyStyleTag(UserStudyStyleTag studyStyleTag) {
        this.studyStyleTags.add(studyStyleTag);
        studyStyleTag.setUser(this);
    }

    public void updateRefreshToken(String refreshToken, LocalDateTime refreshTokenExpiredAt) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
    }

    public void clearRefreshToken() {
        this.refreshToken = null;
        this.refreshTokenExpiredAt = null;
    }
}
