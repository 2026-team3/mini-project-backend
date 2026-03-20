package com.team3.ueic.domain.auth.dto.internal;

import com.team3.ueic.domain.auth.dto.response.ReissueResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReissueResult {
    private ReissueResponseDto responseDto;
    private String refreshToken;
}
