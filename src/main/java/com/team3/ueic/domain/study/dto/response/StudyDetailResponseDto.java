package com.team3.ueic.domain.study.dto.response;

import com.team3.ueic.domain.test.enums.WeakType;
import com.team3.ueic.domain.user.entity.AvailableTime;
import com.team3.ueic.domain.user.entity.PreferredMode;
import com.team3.ueic.domain.user.entity.StudyStyleTagType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyDetailResponseDto {

    private Long studyId;
    private String studyName;

    private Long leaderId;
    private String leaderName;

    private PreferredMode preferredMode;
    private Integer maxMembers;
    private Integer currentMemberCount;
    private Integer targetScore;

    private WeakType weakType;

    private List<StudyStyleTagType> styleTags;
    private List<AvailableTime> availableTimes;
}
