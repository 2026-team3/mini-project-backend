package com.team3.ueic.domain.study.entity;

import com.team3.ueic.domain.user.entity.User;
import com.team3.ueic.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "study_members",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"study_id", "user_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StudyMemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StudyMemberStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public StudyMember(StudyMemberRole role, StudyMemberStatus status, Study study, User user) {
        this.role = role;
        this.status = status;
        this.study = study;
        this.user = user;
    }

    public void assignStudy(Study study) {
        this.study = study;
    }

    public void approve() {
        this.status = StudyMemberStatus.ACTIVE;
    }

    public void reject() {
        this.status = StudyMemberStatus.REJECTED;
    }
}