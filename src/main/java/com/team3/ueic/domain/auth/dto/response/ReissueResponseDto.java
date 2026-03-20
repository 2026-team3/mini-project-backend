package com.team3.ueic.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueResponseDto {
    private String accessToken;
    private String tokenType;
}
