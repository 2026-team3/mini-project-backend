package com.team3.ueic.domain.match;

import com.team3.ueic.domain.study.dto.response.CreateStudyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    //취약분야 + 스타일태그 기준 추천 스터디 상위 5개
    @GetMapping("/weak-type/{userId}")
    public ResponseEntity<List<CreateStudyResponseDto>> recommendByWeakType(
            @PathVariable Long userId) {

        List<CreateStudyResponseDto> recommendations = matchService.recommendByWeakType(userId)
                .stream()
                .limit(5) // 상위 5개
                .toList();

        return ResponseEntity.ok(recommendations);
    }

    //목표점수 + 스타일태그 기준 추천 스터디 상위 5개

    @GetMapping("/target-score/{userId}")
    public ResponseEntity<List<CreateStudyResponseDto>> recommendByTargetScore(
            @PathVariable Long userId) {

        List<CreateStudyResponseDto> recommendations = matchService.recommendByTargetScoreByUser(userId)
                .stream()
                .limit(5) // 상위 5개
                .toList();

        return ResponseEntity.ok(recommendations);
    }
}