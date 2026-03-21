package com.team3.ueic.test.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubmitRequest {
    private Long userId;
    private List<AnswerRequest> answers;
}
