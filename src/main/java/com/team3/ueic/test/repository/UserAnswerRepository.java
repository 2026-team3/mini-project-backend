package com.team3.ueic.test.repository;

import com.team3.ueic.test.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    List<UserAnswer> findByUserId(Long userId);
}
