package com.team3.ueic.domain.study.entity;

import com.team3.ueic.domain.test.enums.WeakType;
import com.team3.ueic.domain.user.entity.PreferredMode;
import com.team3.ueic.domain.user.entity.User;
import com.team3.ueic.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "studies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String studyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PreferredMode preferredMode;

    @Column(nullable = false)
    private Integer maxMembers;

    @Column(nullable = false)
    private Integer targetScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyAvailableTime> availableTimes = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyStyleTag> studyStyleTags = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyMember> members = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WeakType weakType; // 스터디 목표 취약분야


    @Builder
    public Study(String studyName, PreferredMode preferredMode, Integer maxMembers, Integer targetScore, User leader,WeakType weakType) {
        this.studyName = studyName;
        this.preferredMode = preferredMode;
        this.maxMembers = maxMembers;
        this.targetScore = targetScore;
        this.leader = leader;
        this.weakType=weakType;
    }

    public void addAvailableTime(StudyAvailableTime availableTime) {
        this.availableTimes.add(availableTime);
        availableTime.setStudy(this);
    }

    public void addStudyStyleTag(StudyStyleTag studyStyleTag) {
        this.studyStyleTags.add(studyStyleTag);
        studyStyleTag.setStudy(this);
    }

    public void addMember(StudyMember member) {
        this.members.add(member);
        member.setStudy(this);
    }
}
