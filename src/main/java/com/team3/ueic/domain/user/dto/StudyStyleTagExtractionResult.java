package com.team3.ueic.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StudyStyleTagExtractionResult {

    @JsonProperty("tags")
    public List<String> tags;
}
