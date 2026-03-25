package com.team3.ueic.domain.test.dto;

import com.team3.ueic.domain.test.enums.WeakType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class SubmitResponse {
    private int correctCount;      // 맞춘 개수
    private int totalCount;        // 전체 개수
    private WeakType weakType;     // 취약 유형
    private String message;

    private Map<String, Long> correctCountByType; // 분야별 맞춘 개수
}
