package com.team3.ueic.test.controller;

import com.team3.ueic.test.dto.QuestionRequest;
import com.team3.ueic.test.dto.QuestionResponse;
import com.team3.ueic.test.dto.SubmitRequest;

import com.team3.ueic.test.dto.SubmitResponse;
import com.team3.ueic.test.enums.WeakType;
import com.team3.ueic.test.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // ================== 문제 풀기 ==================
    @PostMapping("/submit")
    public SubmitResponse submit(@RequestBody SubmitRequest request) {

        Long userId = 1L; // TODO: 로그인 붙이면 변경

        questionService.submitAnswers(userId, request.getAnswers());

        WeakType weakType = questionService.findWeakType(userId);

        if (weakType == null) {
            return new SubmitResponse(true, null, "모든 문제를 맞췄습니다!");
        } else {
            return new SubmitResponse(false, weakType, "취약 분야 분석 완료");
        }
    }
}

