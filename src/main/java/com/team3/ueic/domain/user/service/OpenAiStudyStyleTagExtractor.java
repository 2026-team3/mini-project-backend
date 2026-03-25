package com.team3.ueic.domain.user.service;

import com.openai.client.OpenAIClient;
import com.openai.core.JsonSchemaLocalValidation;
import com.openai.models.responses.StructuredResponseCreateParams;
import com.team3.ueic.domain.user.dto.StudyStyleTagExtractionResult;
import com.team3.ueic.domain.user.entity.StudyStyleTagType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OpenAiStudyStyleTagExtractor implements StudyStyleTagExtractor {

    private final OpenAIClient openAIClient;

    @Override
    public List<StudyStyleTagType> extractTags(String description) {
        if (description == null || description.isBlank()) {
            return List.of();
        }

        String prompt = buildPrompt(description);

        StructuredResponseCreateParams<StudyStyleTagExtractionResult> params =
                StructuredResponseCreateParams.<StudyStyleTagExtractionResult>builder()
                        .model("gpt-4o-mini")
                        .input(prompt)
                        .text(StudyStyleTagExtractionResult.class, JsonSchemaLocalValidation.NO)
                        .build();

        StudyStyleTagExtractionResult result = openAIClient.responses()
                .create(params)
                .output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .findFirst()
                .orElse(null);

        if (result == null || result.tags == null || result.tags.isEmpty()) {
            return List.of();
        }

        Set<StudyStyleTagType> uniqueTags = new LinkedHashSet<>();

        for (String rawTag : result.tags) {
            StudyStyleTagType parsedTag = parseTag(rawTag);
            if (parsedTag != null) {
                uniqueTags.add(parsedTag);
            }
        }

        return uniqueTags.stream().toList();
    }

    private String buildPrompt(String description) {
        return """
                너는 TOEIC 스터디 스타일 태그 추출기다.

                사용자의 설명을 읽고, 아래 enum 값 중 적절한 태그만 골라라.

                가능한 태그:
                HARD
                CASUAL
                DAILY_CHECK
                WEEKLY_CHECK
                HOMEWORK
                VOCAB_FOCUS
                RC_FOCUS
                LC_FOCUS
                MOCK_TEST
                ATTENDANCE_STRICT
                FEEDBACK_ACTIVE

                규칙:
                1. 반드시 위 enum 값 중에서만 선택할 것
                2. 설명에 근거 없는 태그는 넣지 말 것
                3. 최대 5개까지 반환할 것
                4. 중복 없이 반환할 것
                5. 결과는 {"tags": [...]} 형태로 반환할 것

                사용자 설명:
                "%s"
                """.formatted(description);
    }

    private StudyStyleTagType parseTag(String rawTag) {
        if (rawTag == null || rawTag.isBlank()) {
            return null;
        }

        try {
            return StudyStyleTagType.valueOf(rawTag.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}