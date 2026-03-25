package com.team3.ueic.domain.auth.service;

import com.team3.ueic.domain.auth.dto.internal.LoginResult;
import com.team3.ueic.domain.auth.dto.internal.ReissueResult;
import com.team3.ueic.domain.auth.dto.internal.SignupResult;
import com.team3.ueic.domain.auth.dto.request.LoginRequestDto;
import com.team3.ueic.domain.auth.dto.request.SignupRequestDto;
import com.team3.ueic.domain.auth.dto.response.LoginResponseDto;
import com.team3.ueic.domain.auth.dto.response.ReissueResponseDto;
import com.team3.ueic.domain.auth.dto.response.SignupResponseDto;
import com.team3.ueic.domain.user.entity.*;
import com.team3.ueic.domain.user.repository.UserRepository;
import com.team3.ueic.domain.user.service.StudyStyleTagExtractor;
import com.team3.ueic.global.exception.CustomException;
import com.team3.ueic.global.exception.ErrorCode;
import com.team3.ueic.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final StudyStyleTagExtractor studyStyleTagExtractor;

    public SignupResult signup(SignupRequestDto requestDto) {
        validateSignup(requestDto);

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .build();

        UserProfile userProfile = UserProfile.builder()
                .targetScore(requestDto.getTargetScore())
                .preferredMode(requestDto.getPreferredMode())
                .build();

        user.setUserProfile(userProfile);

        for (var time : requestDto.getAvailableTimes()) {
            user.addAvailableTime(
                    UserAvailableTime.builder()
                            .availableTime(time)
                            .build()
            );
        }

        List<StudyStyleTagType> extractedTags;

        try {
            extractedTags = studyStyleTagExtractor.extractTags(requestDto.getStudyStyleDescription());
        } catch (Exception e) {
            extractedTags = List.of();
            log.error("OpenAI 태그 추출 실패", e);
        }

        for (StudyStyleTagType tag : extractedTags) {
            user.addStudyStyleTag(
                    UserStudyStyleTag.builder()
                            .tag(tag)
                            .build()
            );
        }

        User savedUser = userRepository.save(user);

        String accessToken = jwtProvider.createAccessToken(savedUser.getId(), savedUser.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(savedUser.getId(), savedUser.getEmail());

        savedUser.updateRefreshToken(refreshToken, jwtProvider.getRefreshTokenExpiredAt());

        SignupResponseDto responseDto = SignupResponseDto.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .styleTags(extractedTags)
                .build();

        return SignupResult.builder()
                .responseDto(responseDto)
                .refreshToken(refreshToken)
                .build();
    }

    public LoginResult login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_LOGIN));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_LOGIN);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getEmail());

        user.updateRefreshToken(refreshToken, jwtProvider.getRefreshTokenExpiredAt());

        LoginResponseDto responseDto = LoginResponseDto.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();

        return LoginResult.builder()
                .responseDto(responseDto)
                .refreshToken(refreshToken)
                .build();
    }

    public ReissueResult reissue(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        if (!jwtProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        if (!"refresh".equals(jwtProvider.getTokenType(refreshToken))) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Long userId = jwtProvider.getUserId(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getRefreshToken() == null) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        if (!user.getRefreshToken().equals(refreshToken)) {
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        if (user.getRefreshTokenExpiredAt() == null ||
                user.getRefreshTokenExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        String newAccessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getId(), user.getEmail());

        user.updateRefreshToken(newRefreshToken, jwtProvider.getRefreshTokenExpiredAt());

        ReissueResponseDto responseDto = ReissueResponseDto.builder()
                .accessToken(newAccessToken)
                .tokenType("Bearer")
                .build();

        return ReissueResult.builder()
                .responseDto(responseDto)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.clearRefreshToken();
    }

    private void validateSignup(SignupRequestDto requestDto) {
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
            throw new CustomException(ErrorCode.PHONE_ALREADY_EXISTS);
        }
    }
}
