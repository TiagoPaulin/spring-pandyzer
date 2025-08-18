package com.pandyzer.backend.repositories;

import com.pandyzer.backend.models.Evaluator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EvaluatorRepository extends JpaRepository<Evaluator, Long> {

    List<Evaluator> findByEvaluationId(Long evaluationId);

    @Query("SELECT e FROM Evaluator e WHERE e.user.id = :userId AND e.evaluation.id = :evaluationId")
    Evaluator findByUserIdAndEvaluationId(Long userId, Long evaluationId);

    Integer countByUserIdAndStatusId(Long userId, Long statusId);
}
