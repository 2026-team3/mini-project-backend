package com.team3.ueic.domain.study.repository;

import com.team3.ueic.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}