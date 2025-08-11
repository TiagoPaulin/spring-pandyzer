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

    /**
     * Conta as avaliações em que um usuário é avaliador, o status é 'Em Andamento' (ID 1)
     * e cuja data atual está dentro do período da avaliação.
     */
    @Query("SELECT COUNT(e) FROM Evaluator e " +
            "WHERE e.user.id = :userId " +
            "AND e.status.id = 1 " + // Status "Em Andamento"
            "AND e.evaluation.startDate <= :currentDate " +
            "AND e.evaluation.finalDate >= :currentDate")
    Integer countOngoingForUser(@Param("userId") Long userId, @Param("currentDate") Date currentDate);


    /**
     * Conta as avaliações que um usuário concluiu (status ID 2) dentro de um
     * determinado período de tempo.
     */
    @Query("SELECT COUNT(e) FROM Evaluator e " +
            "WHERE e.user.id = :userId " +
            "AND e.status.id = 2 " + // Status "Concluído"
            "AND e.evaluation.finalDate BETWEEN :startDate AND :endDate")
    Integer countCompletedForUserInPeriod(
            @Param("userId") Long userId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}
