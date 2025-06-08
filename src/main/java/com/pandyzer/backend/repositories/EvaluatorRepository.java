package com.pandyzer.backend.repositories;

import com.pandyzer.backend.models.Evaluator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluatorRepository extends JpaRepository<Evaluator, Long> {

    List<Evaluator> findByEvaluationId(Long evaluationId);

}
