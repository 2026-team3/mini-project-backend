package com.team3.ueic.domain.study.dto.response;

import com.team3.ueic.domain.study.entity.StudyMemberRole;
import com.team3.ueic.domain.test.enums.WeakType;
import com.team3.ueic.domain.user.entity.PreferredMode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyParticipatingStudyResponseDto {
    private Long studyId;
    private String studyName;
    private String leaderName;
    private PreferredMode preferredMode;
    private Integer targetScore;
    private Integer maxMembers;
    private Integer currentMemberCount;
    private WeakType weakType;
    private StudyMemberRole role;
}
