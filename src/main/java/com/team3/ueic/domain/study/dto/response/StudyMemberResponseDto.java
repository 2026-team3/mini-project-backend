package com.team3.ueic.domain.study.dto.response;

import com.team3.ueic.domain.study.entity.StudyMemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyMemberResponseDto {

    private Long userId;
    private String userName;
    private StudyMemberRole role;
}
