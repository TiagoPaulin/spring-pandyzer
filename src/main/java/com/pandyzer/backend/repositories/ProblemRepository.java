package com.pandyzer.backend.repositories;

import com.pandyzer.backend.models.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByObjective_Id(Long objectiveId);

    List<Problem> findByObjective_IdAndUser_Id(Long objectiveId, Long userId);

    // Todos os problemas de uma avaliação (via objective.evaluation)
    @Query("select p from Problem p where p.objective.evaluation.id = :evaluationId")
    List<Problem> findAllByEvaluation(@Param("evaluationId") Long evaluationId);

    // Problemas da avaliação filtrando pelo usuário (avaliador) que reportou
    @Query("""
           select p 
           from Problem p 
           where p.objective.evaluation.id = :evaluationId 
             and p.user.id = :userId
           """)
    List<Problem> findAllByEvaluationAndUser(
            @Param("evaluationId") Long evaluationId,
            @Param("userId") Long userId
    );

}
