package com.team3.ueic.domain.user.controller;

import com.team3.ueic.domain.user.dto.response.MyInfoResponseDto;
import com.team3.ueic.domain.user.service.UserService;
import com.team3.ueic.global.reponse.ApiResponse;
import com.team3.ueic.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyInfoResponseDto>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MyInfoResponseDto responseDto = userService.getMyInfo(userDetails.getUserId());

        return ResponseEntity.ok(
                ApiResponse.success("내 정보 조회 성공", responseDto)
        );
    }
}
