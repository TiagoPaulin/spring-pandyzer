package com.pandyzer.backend.repositories;

import com.pandyzer.backend.models.Objective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObjectiveRepository extends JpaRepository<Objective, Long> {

    List<Objective> findByEvaluationId(Long evaluationId);

}
