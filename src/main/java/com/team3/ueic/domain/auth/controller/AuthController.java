package com.team3.ueic.domain.auth.controller;

import com.team3.ueic.domain.auth.dto.internal.LoginResult;
import com.team3.ueic.domain.auth.dto.internal.ReissueResult;
import com.team3.ueic.domain.auth.dto.internal.SignupResult;
import com.team3.ueic.domain.auth.dto.request.LoginRequestDto;
import com.team3.ueic.domain.auth.dto.request.SignupRequestDto;
import com.team3.ueic.domain.auth.dto.response.LoginResponseDto;
import com.team3.ueic.domain.auth.dto.response.ReissueResponseDto;
import com.team3.ueic.domain.auth.dto.response.SignupResponseDto;
import com.team3.ueic.domain.auth.service.AuthService;
import com.team3.ueic.global.reponse.ApiResponse;
import com.team3.ueic.global.security.CustomUserDetails;
import com.team3.ueic.global.security.JwtProvider;
import com.team3.ueic.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponseDto>> signup(
            @Valid @RequestBody SignupRequestDto requestDto,
            HttpServletResponse response
    ) {
        SignupResult result = authService.signup(requestDto);

        CookieUtil.addRefreshTokenCookie(
                response,
                result.getRefreshToken(),
                jwtProvider.getRefreshTokenExpirationSeconds()
        );

        return ResponseEntity.ok(
                ApiResponse.success("회원가입이 완료되었습니다.", result.getResponseDto())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto requestDto,
            HttpServletResponse response
    ) {
        LoginResult result = authService.login(requestDto);

        CookieUtil.addRefreshTokenCookie(
                response,
                result.getRefreshToken(),
                jwtProvider.getRefreshTokenExpirationSeconds()
        );

        return ResponseEntity.ok(
                ApiResponse.success("로그인에 성공했습니다.", result.getResponseDto())
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<ReissueResponseDto>> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        ReissueResult result = authService.reissue(refreshToken);

        CookieUtil.addRefreshTokenCookie(
                response,
                result.getRefreshToken(),
                jwtProvider.getRefreshTokenExpirationSeconds()
        );

        return ResponseEntity.ok(
                ApiResponse.success("토큰이 재발급되었습니다.", result.getResponseDto())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletResponse response
    ) {
        authService.logout(userDetails.getUserId());
        CookieUtil.deleteRefreshTokenCookie(response);

        return ResponseEntity.ok(
                ApiResponse.success("로그아웃이 완료되었습니다.", null)
        );
    }
}
