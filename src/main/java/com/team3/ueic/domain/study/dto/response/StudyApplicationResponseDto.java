package com.team3.ueic.domain.study.dto.response;

import com.team3.ueic.domain.study.entity.StudyMemberStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyApplicationResponseDto {

    private Long studyMemberId;
    private Long userId;
    private String userName;
    private StudyMemberStatus status;
}
