package com.team3.ueic.domain.user.service;

import com.team3.ueic.domain.user.dto.response.MyInfoResponseDto;
import com.team3.ueic.domain.user.entity.User;
import com.team3.ueic.domain.user.entity.UserProfile;
import com.team3.ueic.domain.user.repository.UserRepository;
import com.team3.ueic.global.exception.CustomException;
import com.team3.ueic.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public MyInfoResponseDto getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserProfile userProfile = user.getUserProfile();

        return MyInfoResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .targetScore(userProfile.getTargetScore())
                .weakType(userProfile.getWeakType())
                .preferredMode(userProfile.getPreferredMode())
                .build();
    }
}
