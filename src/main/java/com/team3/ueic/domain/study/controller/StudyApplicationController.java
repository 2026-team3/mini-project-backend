package com.team3.ueic.domain.study.controller;

import com.team3.ueic.domain.study.dto.response.StudyApplicationResponseDto;
import com.team3.ueic.domain.study.service.StudyApplicationService;
import com.team3.ueic.global.reponse.ApiResponse;
import com.team3.ueic.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studies")
public class StudyApplicationController {

    private final StudyApplicationService studyApplicationService;

    @PostMapping("/{studyId}/apply")
    public ResponseEntity<ApiResponse<Void>> applyToStudy(
            @PathVariable Long studyId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        studyApplicationService.applyToStudy(userDetails.getUserId(), studyId);

        return ResponseEntity.ok(
                ApiResponse.success("스터디 신청이 완료되었습니다.", null)
        );
    }

    @GetMapping("/{studyId}/applications")
    public ResponseEntity<ApiResponse<List<StudyApplicationResponseDto>>> getPendingApplications(
            @PathVariable Long studyId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<StudyApplicationResponseDto> response =
                studyApplicationService.getPendingApplications(userDetails.getUserId(), studyId);

        return ResponseEntity.ok(
                ApiResponse.success("스터디 신청 목록 조회가 완료되었습니다.", response)
        );
    }

    @PatchMapping("/{studyId}/applications/{studyMemberId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveApplication(
            @PathVariable Long studyId,
            @PathVariable Long studyMemberId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        studyApplicationService.approveApplication(userDetails.getUserId(), studyId, studyMemberId);

        return ResponseEntity.ok(
                ApiResponse.success("스터디 신청 승인 완료", null)
        );
    }

    @PatchMapping("/{studyId}/applications/{studyMemberId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectApplication(
            @PathVariable Long studyId,
            @PathVariable Long studyMemberId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        studyApplicationService.rejectApplication(userDetails.getUserId(), studyId, studyMemberId);

        return ResponseEntity.ok(
                ApiResponse.success("스터디 신청 거절 완료", null)
        );
    }
}
