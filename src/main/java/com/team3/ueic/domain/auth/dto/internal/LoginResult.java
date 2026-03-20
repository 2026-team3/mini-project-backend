package com.team3.ueic.domain.auth.dto.internal;

import com.team3.ueic.domain.auth.dto.response.LoginResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResult {
    private LoginResponseDto responseDto;
    private String refreshToken;
}
