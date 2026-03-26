package com.team3.ueic.domain.match;

import com.team3.ueic.domain.study.entity.Study;
import com.team3.ueic.domain.user.entity.StudyStyleTagType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class StudyScoreDto {
    private Study study;
    private Set<StudyStyleTagType> tags;
    private double score;
}
