package com.team3.ueic.domain.user.entity;

import com.team3.ueic.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_study_style_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStudyStyleTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StudyStyleTagType tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public UserStudyStyleTag(StudyStyleTagType tag) {
        this.tag = tag;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
