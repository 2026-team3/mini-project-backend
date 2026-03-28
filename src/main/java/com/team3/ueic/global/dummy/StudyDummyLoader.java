package com.team3.ueic.global.dummy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.ueic.domain.study.entity.*;
import com.team3.ueic.domain.study.repository.StudyRepository;
import com.team3.ueic.domain.test.enums.WeakType;
import com.team3.ueic.domain.user.entity.AvailableTime;
import com.team3.ueic.domain.user.entity.PreferredMode;
import com.team3.ueic.domain.user.entity.StudyStyleTagType;
import com.team3.ueic.domain.user.entity.User;
import com.team3.ueic.domain.user.repository.UserRepository;
import com.team3.ueic.global.dummy.dto.StudyDummyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StudyDummyLoader implements CommandLineRunner {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (studyRepository.count() > 0) {
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        InputStream inputStream = new ClassPathResource("studies.json").getInputStream();

        List<StudyDummyDto> studies = objectMapper.readValue(
                inputStream,
                new TypeReference<List<StudyDummyDto>>() {}
        );

        for (StudyDummyDto dto : studies) {
            User leader = userRepository.findById(dto.getLeaderId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 leaderId: " + dto.getLeaderId()));

            Study study = Study.builder()
                    .studyName(dto.getStudyName())
                    .preferredMode(PreferredMode.valueOf(dto.getPreferredMode()))
                    .maxMembers(dto.getMaxMembers())
                    .targetScore(dto.getTargetScore())
                    .leader(leader)
                    .weakType(WeakType.valueOf(dto.getWeakType()))
                    .build();

            if (dto.getAvailableTimes() != null) {
                for (String time : dto.getAvailableTimes()) {
                    StudyAvailableTime availableTime = StudyAvailableTime.builder()
                            .availableTime(AvailableTime.valueOf(time))
                            .build();
                    study.addAvailableTime(availableTime);
                }
            }

            if (dto.getStudyStyleTags() != null) {
                for (String tagValue : dto.getStudyStyleTags()) {
                    StudyStyleTag tag = StudyStyleTag.builder()
                            .tag(StudyStyleTagType.valueOf(tagValue))
                            .build();
                    study.addStudyStyleTag(tag);
                }
            }

            if (dto.getMembers() != null) {
                for (StudyDummyDto.MemberDto memberDto : dto.getMembers()) {
                    User user = userRepository.findById(memberDto.getUserId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId: " + memberDto.getUserId()));

                    StudyMember member = StudyMember.builder()
                            .user(user)
                            .role(StudyMemberRole.valueOf(memberDto.getRole()))
                            .status(StudyMemberStatus.valueOf(memberDto.getStatus()))
                            .build();

                    study.addMember(member);
                }
            }

            studyRepository.save(study);
        }
    }
}