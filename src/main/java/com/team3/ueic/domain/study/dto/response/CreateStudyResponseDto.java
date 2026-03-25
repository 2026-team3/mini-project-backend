package com.team3.ueic.domain.study.dto.response;

import com.team3.ueic.domain.user.entity.StudyStyleTagType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreateStudyResponseDto {
    private Long studyId;
    private String studyName;
    private Long leaderId;
    private String leaderName;
    private List<StudyStyleTagType> styleTags;
}
