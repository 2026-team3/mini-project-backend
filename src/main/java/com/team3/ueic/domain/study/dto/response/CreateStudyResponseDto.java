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
public class CreateStudyResponseDto {
    private Long studyId;
    private String studyName;
    private Long leaderId;
    private String leaderName;
    private List<StudyStyleTagType> styleTags;

    // 추가 필드
    private PreferredMode preferredMode; // 온/오프라인
    private Integer maxMembers;           // 최대 모집 인원
    private Integer targetScore;          // 목표 점수
    private List<AvailableTime> availableTimes; // 가능한 시간대
    private WeakType weakType;            // 취약분야
    private Integer currentMemberCount;   // 현재 멤버 수
}