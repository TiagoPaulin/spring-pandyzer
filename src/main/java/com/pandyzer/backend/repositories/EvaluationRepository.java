package com.pandyzer.backend.repositories;

import com.pandyzer.backend.models.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    @Query(value = """
    SELECT DISTINCT e.* FROM avaliacao e
    LEFT JOIN evaluator ev ON ev.avaliacao_id = e.avaliacao_id
    WHERE (:description IS NULL OR LOWER(e.descricao) LIKE LOWER(CONCAT('%', :description, '%')))
      AND (:startDate IS NULL OR e.data_inicial >= :startDate AND e.data_inicial < DATE_ADD(:startDate, INTERVAL 1 DAY))
      AND (:finalDate IS NULL OR e.data_final >= :finalDate AND e.data_final < DATE_ADD(:finalDate, INTERVAL 1 DAY))
      AND (:statusId IS NULL OR ev.status_id = :statusId)
""", nativeQuery = true)
    List<Evaluation> findByFilters(
            @Param("description") String description,
            @Param("startDate") Date startDate,
            @Param("finalDate") Date finalDate,
            @Param("statusId") Long statusId
    );

    @Query(value = """
        SELECT COUNT(*)
        FROM evaluator ev
        INNER JOIN status s ON ev.status_id = s.status_id
        WHERE ev.avaliacao_id = :evaluationId
          AND LOWER(s.descricao) = 'concluído'
    """, nativeQuery = true)
    Integer countEvaluatorsWithConcludedStatus(@Param("evaluationId") Long evaluationId);

}
