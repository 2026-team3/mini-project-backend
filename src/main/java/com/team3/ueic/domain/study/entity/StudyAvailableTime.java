package com.team3.ueic.domain.study.entity;

import com.team3.ueic.domain.user.entity.AvailableTime;
import com.team3.ueic.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "study_available_times")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyAvailableTime extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AvailableTime availableTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Builder
    public StudyAvailableTime(AvailableTime availableTime) {
        this.availableTime = availableTime;
    }

    public void setStudy(Study study) {
        this.study = study;
    }
}
