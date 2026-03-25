package com.team3.ueic.domain.user.service;

import com.team3.ueic.domain.user.entity.StudyStyleTagType;

import java.util.List;

public interface StudyStyleTagExtractor {
    List<StudyStyleTagType> extractTags(String description);
}
