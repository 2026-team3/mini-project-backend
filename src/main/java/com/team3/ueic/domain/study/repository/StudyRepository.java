package com.team3.ueic.domain.study.repository;

import com.team3.ueic.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {

    @Query("""
        SELECT DISTINCT s
        FROM Study s
        JOIN FETCH s.leader
        LEFT JOIN FETCH s.studyStyleTags st
    """)
    List<Study> findAllWithLeaderAndTags();
}