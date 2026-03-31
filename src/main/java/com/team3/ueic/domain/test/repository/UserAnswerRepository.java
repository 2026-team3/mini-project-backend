package com.team3.ueic.domain.test.repository;

import com.team3.ueic.domain.test.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    Optional<UserAnswer> findByUserIdAndQuestionId(Long userId, Long questionId);

    List<UserAnswer> findByUserId(Long userId);

    List<UserAnswer> findByUserIdAndCorrectTrue(Long userId);

    // =================== 최신 기록 기준 유형별 맞춘 개수 ===================
    @Query("""
    SELECT a.question.weakType, COUNT(a)
    FROM UserAnswer a
    WHERE a.userId = :userId
      AND a.correct = false
      AND a.createdAt = (
          SELECT MAX(u.createdAt)
          FROM UserAnswer u
          WHERE u.userId = a.userId
            AND u.question.id = a.question.id
      )
    GROUP BY a.question.weakType
""")
    List<Object[]> countLatestCorrectByType(@Param("userId") Long userId);
}