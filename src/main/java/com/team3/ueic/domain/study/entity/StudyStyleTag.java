package com.team3.ueic.domain.study.entity;

import com.team3.ueic.domain.user.entity.StudyStyleTagType;
import com.team3.ueic.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "study_style_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyStyleTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StudyStyleTagType tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @Builder
    public StudyStyleTag(StudyStyleTagType tag) {
        this.tag = tag;
    }

    public void setStudy(Study study) {
        this.study = study;
    }
}
