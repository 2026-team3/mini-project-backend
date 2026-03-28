package com.team3.ueic.domain.study.service;

import com.team3.ueic.domain.study.dto.response.StudyApplicationResponseDto;
import com.team3.ueic.domain.study.entity.Study;
import com.team3.ueic.domain.study.entity.StudyMember;
import com.team3.ueic.domain.study.entity.StudyMemberRole;
import com.team3.ueic.domain.study.entity.StudyMemberStatus;
import com.team3.ueic.domain.study.repository.StudyMemberRepository;
import com.team3.ueic.domain.study.repository.StudyRepository;
import com.team3.ueic.domain.user.entity.User;
import com.team3.ueic.domain.user.repository.UserRepository;
import com.team3.ueic.global.exception.CustomException;
import com.team3.ueic.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyApplicationService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public void applyToStudy(Long userId, Long studyId) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (study.getLeader().getId().equals(userId)) {
            throw new CustomException(ErrorCode.STUDY_LEADER_CANNOT_APPLY);
        }

        boolean exists = studyMemberRepository.existsByStudyAndUser(study, user);
        if (exists) {
            throw new CustomException(ErrorCode.STUDY_ALREADY_APPLIED);
        }

        StudyMember studyMember = StudyMember.builder()
                .study(study)
                .user(user)
                .role(StudyMemberRole.MEMBER)
                .status(StudyMemberStatus.PENDING)
                .build();

        studyMemberRepository.save(studyMember);
    }

    @Transactional(readOnly = true)
    public List<StudyApplicationResponseDto> getPendingApplications(Long leaderId, Long studyId) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        if (!study.getLeader().getId().equals(leaderId)) {
            throw new CustomException(ErrorCode.STUDY_ACCESS_DENIED);
        }

        List<StudyMember> pendingMembers =
                studyMemberRepository.findByStudyAndStatus(study, StudyMemberStatus.PENDING);

        return pendingMembers.stream()
                .map(member -> StudyApplicationResponseDto.builder()
                        .studyMemberId(member.getId())
                        .userId(member.getUser().getId())
                        .userName(member.getUser().getName())
                        .status(member.getStatus())
                        .build())
                .toList();
    }

    @Transactional
    public void approveApplication(Long leaderId, Long studyId, Long studyMemberId) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        if (!study.getLeader().getId().equals(leaderId)) {
            throw new CustomException(ErrorCode.STUDY_ACCESS_DENIED);
        }

        StudyMember studyMember = studyMemberRepository.findByIdAndStudy(studyMemberId, study)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_APPLICATION_NOT_FOUND));

        if (studyMember.getStatus() != StudyMemberStatus.PENDING) {
            throw new CustomException(ErrorCode.STUDY_APPLICATION_ALREADY_PROCESSED);
        }

        int activeCount = studyMemberRepository.countByStudyAndStatus(study, StudyMemberStatus.ACTIVE);

        if (activeCount >= study.getMaxMembers()) {
            throw new CustomException(ErrorCode.STUDY_FULL);
        }

        studyMember.approve();
    }

    @Transactional
    public void rejectApplication(Long leaderId, Long studyId, Long studyMemberId) {

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        if (!study.getLeader().getId().equals(leaderId)) {
            throw new CustomException(ErrorCode.STUDY_ACCESS_DENIED);
        }

        StudyMember studyMember = studyMemberRepository.findByIdAndStudy(studyMemberId, study)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDY_APPLICATION_NOT_FOUND));

        if (studyMember.getStatus() != StudyMemberStatus.PENDING) {
            throw new CustomException(ErrorCode.STUDY_APPLICATION_ALREADY_PROCESSED);
        }

        studyMember.reject();
    }
}
