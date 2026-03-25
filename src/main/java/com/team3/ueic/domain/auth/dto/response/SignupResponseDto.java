package com.team3.ueic.domain.auth.dto.response;

import com.team3.ueic.domain.user.entity.StudyStyleTagType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SignupResponseDto {
    private String accessToken;
    private String tokenType;
    private Long userId;
    private String email;
    private String name;
    private List<StudyStyleTagType> styleTags;
}
