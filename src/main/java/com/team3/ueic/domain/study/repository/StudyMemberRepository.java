package com.team3.ueic.domain.study.repository;

import com.team3.ueic.domain.study.entity.Study;
import com.team3.ueic.domain.study.entity.StudyMember;
import com.team3.ueic.domain.study.entity.StudyMemberStatus;
import com.team3.ueic.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    boolean existsByStudyAndUser(Study study, User user);

    List<StudyMember> findByStudyAndStatus(Study study, StudyMemberStatus status);

    Integer countByStudyAndStatus(Study study, StudyMemberStatus status);

    Optional<StudyMember> findByIdAndStudy(Long id, Study study);
}
