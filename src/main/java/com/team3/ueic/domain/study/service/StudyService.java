package com.team3.ueic.domain.study.service;

import com.team3.ueic.domain.study.dto.request.CreateStudyRequestDto;
import com.team3.ueic.domain.study.dto.response.CreateStudyResponseDto;
import com.team3.ueic.domain.study.dto.response.StudyDetailResponseDto;
import com.team3.ueic.domain.study.dto.response.StudyMemberResponseDto;
import com.team3.ueic.domain.study.entity.*;
import com.team3.ueic.domain.study.repository.StudyMemberRepository;
import com.team3.ueic.domain.study.repository.StudyRepository;
import com.team3.ueic.domain.user.entity.StudyStyleTagType;
import com.team3.ueic.domain.user.entity.User;
import com.team3.ueic.domain.user.repository.UserRepository;
import com.team3.ueic.domain.user.service.StudyStyleTagExtractor;
import com.team3.ueic.global.exception.CustomException;
import com.team3.ueic.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyStyleTagExtractor studyStyleTagExtractor;
    private final StudyMemberRepository studyMemberRepository;

    public CreateStudyResponseDto createStudy(Long userId, CreateStudyRequestDto requestDto) {
        User leader = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Study study = Study.builder()
                .studyName(requestDto.getStudyName())
                .preferredMode(requestDto.getPreferredMode())
                .maxMembers(requestDto.getMaxMembers())
                .targetScore(requestDto.getTargetScore())
                .weakType(requestDto.getWeakType())
                .leader(leader)
                .build();

        StudyMember leaderMember = StudyMember.builder()
                .user(leader)
                .role(StudyMemberRole.LEADER)
                .status(StudyMemberStatus.ACTIVE)
                .build();

        study.addMember(leaderMember);

        for (var time : requestDto.getAvailableTimes()) {
            study.addAvailableTime(
                    StudyAvailableTime.builder()
                            .availableTime(time)
                            .build()
            );
        }

        List<StudyStyleTagType> extractedTags;
        try {
            extractedTags = studyStyleTagExtractor.extractTags(requestDto.getStudyStyleDescription());
        } catch (Exception e) {
            log.error("스터디 스타일 태그 추출 실패", e);
            extractedTags = List.of();
        }

        for (StudyStyleTagType tag : extractedTags) {
            study.addStudyStyleTag(
                    StudyStyleTag.builder()
                            .tag(tag)
                            .build()
            );
        }

        Study savedStudy = studyRepository.save(study);

        return CreateStudyResponseDto.builder()
                .studyId(savedStudy.getId())
                .studyName(savedStudy.getStudyName())
                .leaderId(savedStudy.getLeader().getId())
                .leaderName(savedStudy.getLeader().getName())
                .styleTags(extractedTags)
                .preferredMode(savedStudy.getPreferredMode())
                .maxMembers(savedStudy.getMaxMembers())
                .targetScore(savedStudy.getTargetScore())
                .availableTimes(
                        savedStudy.getAvailableTimes().stream()
                                .map(StudyAvailableTime::getAvailableTime)
                                .toList()
                )
                .weakType(savedStudy.getWeakType())
                .currentMemberCount(savedStudy.getMembers().size())
                .build();
    }

    @Transactional(readOnly = true)
    public StudyDetailResponseDto getStudyDetail(Long studyId) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        int currentMemberCount =
                studyMemberRepository.countByStudyAndStatus(study, StudyMemberStatus.ACTIVE);

        return StudyDetailResponseDto.builder()
                .studyId(study.getId())
                .studyName(study.getStudyName())
                .leaderId(study.getLeader().getId())
                .leaderName(study.getLeader().getName())
                .preferredMode(study.getPreferredMode())
                .maxMembers(study.getMaxMembers())
                .currentMemberCount(currentMemberCount)
                .targetScore(study.getTargetScore())
                .weakType(study.getWeakType())
                .styleTags(
                        study.getStudyStyleTags().stream()
                                .map(tag -> tag.getTag())
                                .toList()
                )
                .availableTimes(
                        study.getAvailableTimes().stream()
                                .map(time -> time.getAvailableTime())
                                .toList()
                )
                .build();
    }

    @Transactional(readOnly = true)
    public List<StudyMemberResponseDto> getStudyMembers(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        List<StudyMember> activeMembers =
                studyMemberRepository.findByStudyAndStatus(study, StudyMemberStatus.ACTIVE);

        return activeMembers.stream()
                .map(member -> StudyMemberResponseDto.builder()
                        .userId(member.getUser().getId())
                        .userName(member.getUser().getName())
                        .role(member.getRole())
                        .build())
                .toList();
    }
}