package com.team3.ueic.domain.study.controller;

import com.team3.ueic.domain.study.dto.request.CreateStudyRequestDto;
import com.team3.ueic.domain.study.dto.response.CreateStudyResponseDto;
import com.team3.ueic.domain.study.service.StudyService;
import com.team3.ueic.global.reponse.ApiResponse;
import com.team3.ueic.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies")
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateStudyResponseDto>> createStudy(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateStudyRequestDto requestDto
    ) {
        CreateStudyResponseDto responseDto =
                studyService.createStudy(userDetails.getUserId(), requestDto);

        return ResponseEntity.ok(
                ApiResponse.success("스터디 생성이 완료되었습니다.", responseDto)
        );
    }
}
