package com.team3.ueic.domain.test.controller;

import com.team3.ueic.global.security.CustomUserDetails;
import com.team3.ueic.domain.test.dto.QuestionRequest;
import com.team3.ueic.domain.test.dto.QuestionResponse;
import com.team3.ueic.domain.test.dto.SubmitRequest;

import com.team3.ueic.domain.test.dto.SubmitResponse;
import com.team3.ueic.domain.test.enums.WeakType;
import com.team3.ueic.domain.test.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // ================== 문제 생성 ==================
    @PostMapping
    public Long createQuestion(@RequestBody QuestionRequest request) {
        return questionService.createQuestion(
                request.getContent(),
                request.getWeakType(),
                request.getChoices(),
                request.getAnswerIndex()
        );
    }

    // ================== 단건 조회 ==================
    @GetMapping("/{id}")
    public QuestionResponse getQuestion(@PathVariable Long id) {
        return questionService.getQuestion(id);
    }

    // ================== 유형별 조회 ==================
    @GetMapping("/type/{weakType}")
    public List<QuestionResponse> getQuestionsByType(@PathVariable WeakType weakType) {
        return questionService.getQuestionsByType(weakType);
    }

    // ================== 랜덤 문제 조회 ==================
    @GetMapping("/random")
    public List<QuestionResponse> getRandomQuestions(
            @RequestParam(defaultValue = "1") int countPerType) {
        return questionService.getRandomQuestions(countPerType);
    }

    @PostMapping("/submit")
    public SubmitResponse submit(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestBody SubmitRequest request) {

        if (userDetails == null) {
            throw new IllegalArgumentException("로그인된 사용자만 접근 가능합니다.");
        }

        return questionService.submitAnswersAndAnalyze(
                userDetails.getUserId(),
                request.getAnswers()
        );
    }
}
