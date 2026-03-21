package com.team3.ueic.test.service;



import com.team3.ueic.test.dto.ChoiceResponse;
import com.team3.ueic.test.dto.QuestionResponse;
import com.team3.ueic.test.enums.WeakType;
import com.team3.ueic.test.entity.Choice;
import com.team3.ueic.test.entity.Question;
import com.team3.ueic.test.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;

    // ================== 문제 생성 ==================
    @Transactional
    public Long createQuestion(String content, WeakType weakType,
                               List<String> choiceContents, int answerIndex) {

        if (choiceContents == null || choiceContents.size() < 2) {
            throw new IllegalArgumentException("보기는 최소 2개 이상 필요");
        }

        if (answerIndex < 0 || answerIndex >= choiceContents.size()) {
            throw new IllegalArgumentException("잘못된 정답 인덱스");
        }

        Question question = new Question();
        question.setContent(content);
        question.setWeakType(weakType);

        List<Choice> choices = new ArrayList<>();

        for (String choiceContent : choiceContents) {
            Choice choice = new Choice();
            choice.setContent(choiceContent);
            choice.setQuestion(question); // 연관관계 설정
            choices.add(choice);
        }

        question.setChoices(choices);
        question.setAnswerChoice(choices.get(answerIndex)); // 정답 설정

        return questionRepository.save(question).getId();
    }

    // ================== 단건 조회 ==================
    public QuestionResponse getQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("문제 없음"));
        return toResponse(question);
    }

    // ================== 유형별 조회 ==================
    public List<QuestionResponse> getQuestionsByType(WeakType weakType) {
        List<Question> questions = questionRepository.findByWeakType(weakType);
        return questions.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ================== 랜덤 문제 조회 ==================
    public List<QuestionResponse> getRandomQuestions(int countPerType) {
        List<QuestionResponse> result = new ArrayList<>();

        for (WeakType type : WeakType.values()) {
            List<Question> questions = questionRepository.findByWeakType(type);
            if (questions == null || questions.isEmpty()) continue;

            Collections.shuffle(questions);
            int limit = Math.min(countPerType, questions.size());

            result.addAll(
                    questions.subList(0, limit).stream()
                            .map(this::toResponse)
                            .collect(Collectors.toList())
            );
        }

        Collections.shuffle(result);
        return result;
    }

    // ================== 엔티티 → DTO 변환 ==================
    private QuestionResponse toResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setContent(question.getContent());
        response.setWeakType(question.getWeakType());

        // Choice 변환
        List<ChoiceResponse> choiceResponses = question.getChoices().stream()
                .map(c -> new ChoiceResponse(c.getId(), c.getContent()))
                .collect(Collectors.toList());
        response.setChoices(choiceResponses);

        // AnswerChoice 변환
        if (question.getAnswerChoice() != null) {
            response.setAnswerChoice(new ChoiceResponse(
                    question.getAnswerChoice().getId(),
                    question.getAnswerChoice().getContent()
            ));
        }

        return response;
    }
}


