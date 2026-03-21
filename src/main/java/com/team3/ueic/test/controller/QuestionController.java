package com.team3.ueic.test.controller;

import com.team3.ueic.test.dto.QuestionRequest;
import com.team3.ueic.test.dto.QuestionResponse;
import com.team3.ueic.test.entity.Question;
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
        return questionService.getQuestion(id); // DTO 반환
    }

    // ================== 유형별 조회 ==================
    @GetMapping("/type/{weakType}")
    public List<QuestionResponse> getQuestionsByType(@PathVariable WeakType weakType) {
        return questionService.getQuestionsByType(weakType); // DTO 반환
    }

    // ================== 랜덤 문제 조회 ==================
    @GetMapping("/random")
    public List<QuestionResponse> getRandomQuestions(@RequestParam(defaultValue = "1") int countPerType) {
        return questionService.getRandomQuestions(countPerType);
    }
}
