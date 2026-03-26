package com.team3.ueic.global.dummy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudyDummyDto {
    private String studyName;
    private String preferredMode;
    private Integer maxMembers;
    private Integer targetScore;
    private Long leaderId;
    private String weakType;
    private List<String> availableTimes;
    private List<String> studyStyleTags;
    private List<MemberDto> members;

    @Getter
    @Setter
    public static class MemberDto {
        private Long userId;
        private String role;
        private String status;
    }
}
