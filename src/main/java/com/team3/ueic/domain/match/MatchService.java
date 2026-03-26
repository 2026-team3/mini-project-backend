package com.team3.ueic.domain.match;

import com.team3.ueic.domain.study.dto.response.CreateStudyResponseDto;
import com.team3.ueic.domain.study.entity.Study;
import com.team3.ueic.domain.study.entity.StudyStyleTag;
import com.team3.ueic.domain.study.repository.StudyRepository;
import com.team3.ueic.domain.test.enums.WeakType;
import com.team3.ueic.domain.user.entity.StudyStyleTagType;
import com.team3.ueic.domain.user.entity.UserProfile;
import com.team3.ueic.domain.user.entity.UserStudyStyleTag;
import com.team3.ueic.domain.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final StudyRepository studyRepository;
    private final UserProfileRepository userProfileRepository;


    // 취약분야 + 스타일태그 추천

    public List<CreateStudyResponseDto> recommendByWeakType(Long userId) {
        UserProfile profile = getUserProfile(userId);
        WeakType userWeakType = profile.getWeakType();
        Set<StudyStyleTagType> userTags = profile.getUser().getStudyStyleTags().stream()
                .map(UserStudyStyleTag::getTag)
                .collect(Collectors.toSet());

        List<Study> allStudies = studyRepository.findAllWithLeaderAndTags();

        return allStudies.stream()
                .map(study -> {
                    Set<StudyStyleTagType> studyTags = study.getStudyStyleTags().stream()
                            .map(StudyStyleTag::getTag)
                            .collect(Collectors.toSet());

                    double score = calculateWeakTypeScore(userWeakType, userTags, study, studyTags);
                    return new StudyScoreDto(study, studyTags, score);
                })
                .sorted(Comparator.comparingDouble(StudyScoreDto::getScore).reversed())
                .map(this::toResponseDto)
                .toList();
    }

    private double calculateWeakTypeScore(WeakType userWeakType,
                                          Set<StudyStyleTagType> userTags,
                                          Study study,
                                          Set<StudyStyleTagType> studyTags) {
        double score = 0;

        // 0.7 취약분야
        if (study.getWeakType() == userWeakType) {
            score += 0.7;
        }

        // 0.3 태그 유사도
        double jaccard = jaccardSimilarity(userTags, studyTags);
        score += 0.3 * jaccard;

        return score;
    }


    //  목표점수 + 스타일태그 추천

    public List<CreateStudyResponseDto> recommendByTargetScoreByUser(Long userId) {
        UserProfile profile = getUserProfile(userId);
        Integer targetScore = profile.getTargetScore(); // Integer로 변경
        Set<StudyStyleTagType> userTags = profile.getUser().getStudyStyleTags().stream()
                .map(UserStudyStyleTag::getTag)
                .collect(Collectors.toSet());

        List<Study> allStudies = studyRepository.findAllWithLeaderAndTags();

        return allStudies.stream()
                .map(study -> {
                    Set<StudyStyleTagType> studyTags = study.getStudyStyleTags().stream()
                            .map(StudyStyleTag::getTag)
                            .collect(Collectors.toSet());

                    double score = calculateTargetScoreScore(targetScore, userTags, study, studyTags);
                    return new StudyScoreDto(study, studyTags, score);
                })
                .sorted(Comparator.comparingDouble(StudyScoreDto::getScore).reversed())
                .map(this::toResponseDto)
                .toList();
    }

    private double calculateTargetScoreScore(Integer targetScore,
                                             Set<StudyStyleTagType> userTags,
                                             Study study,
                                             Set<StudyStyleTagType> studyTags) {

        double score = 0;

        Integer studyTargetScore = study.getTargetScore(); // Integer 사용
        if (targetScore != null && studyTargetScore != null) {
            double diff = Math.abs(targetScore - studyTargetScore);
            double targetScoreMatch = 1 - (diff / Math.max(targetScore, studyTargetScore)); // 0~1
            score += 0.7 * targetScoreMatch;
        }

        // 0.3 태그 유사도
        double jaccard = jaccardSimilarity(userTags, studyTags);
        score += 0.3 * jaccard;

        return score;
    }

    // ---------------------------
    // 공통 유틸
    // ---------------------------
    private UserProfile getUserProfile(Long userId) {
        return userProfileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("UserProfile not found"));
    }

    private CreateStudyResponseDto toResponseDto(StudyScoreDto dto) {
        return CreateStudyResponseDto.builder()
                .studyId(dto.getStudy().getId())
                .studyName(dto.getStudy().getStudyName())
                .leaderId(dto.getStudy().getLeader().getId())
                .leaderName(dto.getStudy().getLeader().getName())
                .styleTags(new ArrayList<>(dto.getTags()))
                .build();
    }

    private double jaccardSimilarity(Set<StudyStyleTagType> set1,
                                     Set<StudyStyleTagType> set2) {
        if (set1.isEmpty() && set2.isEmpty()) return 1.0;

        Set<StudyStyleTagType> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<StudyStyleTagType> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }
}