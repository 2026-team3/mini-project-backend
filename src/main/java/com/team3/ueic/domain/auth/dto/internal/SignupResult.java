package com.team3.ueic.domain.auth.dto.internal;

import com.team3.ueic.domain.auth.dto.response.SignupResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResult {
    private SignupResponseDto responseDto;
    private String refreshToken;
}
