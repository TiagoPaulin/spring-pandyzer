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

    // ✅ NOVO: mesmo filtro, mas com creatorId opcional
    @Query(value = """
    SELECT DISTINCT e.* FROM avaliacao e
    LEFT JOIN evaluator ev ON ev.avaliacao_id = e.avaliacao_id
    WHERE (:description IS NULL OR LOWER(e.descricao) LIKE LOWER(CONCAT('%', :description, '%')))
      AND (:startDate IS NULL OR e.data_inicial >= :startDate AND e.data_inicial < DATE_ADD(:startDate, INTERVAL 1 DAY))
      AND (:finalDate IS NULL OR e.data_final >= :finalDate AND e.data_final < DATE_ADD(:finalDate, INTERVAL 1 DAY))
      AND (:statusId IS NULL OR ev.status_id = :statusId)
      AND (:creatorId IS NULL OR e.usuario_id = :creatorId)
""", nativeQuery = true)
    List<Evaluation> findByFiltersWithCreator(
            @Param("description") String description,
            @Param("startDate") Date startDate,
            @Param("finalDate") Date finalDate,
            @Param("statusId") Long statusId,
            @Param("creatorId") Long creatorId
    );

    @Query(value = """
        SELECT COUNT(*)
        FROM evaluator ev
        INNER JOIN status s ON ev.status_id = s.status_id
        WHERE ev.avaliacao_id = :evaluationId
          AND LOWER(s.descricao) = 'concluído'
    """, nativeQuery = true)
    Integer countEvaluatorsWithConcludedStatus(@Param("evaluationId") Long evaluationId);

    List<Evaluation> findByUser_Id(Long userId);

    List<Evaluation> findByIsPublicTrueAndUser_IdNot(Long userId);

    Integer countByUserIdAndRegisterBetween(Long userId, Date startDate, Date endDate);

    // (Opcional ter as duas assinaturas; se não usar a de Integer, pode remover)
    List<Evaluation> findByUser_Id(Integer userId);

    @Query("select e from Evaluation e where e.user.id = :creatorId")
    List<Evaluation> findByCreatorId(Long creatorId);

    @Query("select count(e) from Evaluation e where e.user.id = :userId")
    long countByUserId(Long userId);

    // EvaluationRepository.java
    @Query("select distinct e from Evaluation e join e.evaluators ev where ev.user.id = :userId")
    List<Evaluation> findByEvaluatorUserId(@Param("userId") Long userId);

    /**
     * Filtro por título (descricao), criador (usuario_id) e STATUS GERAL da avaliação.
     * statusId:
     *   1 = Em andamento
     *   2 = Concluída
     *   3 = Não iniciada
     *
     * Regras:
     *   - Não iniciada: COUNT(ev)=0 OR SUM(status=3)=COUNT(ev)
     *   - Concluída:    COUNT(ev)>0 AND SUM(status=2)=COUNT(ev)
     *   - Em andamento: o restante (mistura / alguém começou e não terminou)
     */
    @Query(value = """
        SELECT e.*
        FROM avaliacao e
        LEFT JOIN evaluator ev ON ev.avaliacao_id = e.avaliacao_id
        WHERE (:description IS NULL OR LOWER(e.descricao) LIKE LOWER(CONCAT('%', :description, '%')))
          AND (:creatorId IS NULL OR e.usuario_id = :creatorId)
        GROUP BY e.avaliacao_id
        HAVING
          (:statusId IS NULL)
          OR (
                -- Não iniciada
                (:statusId = 3 AND (
                    COUNT(ev.avaliacao_id) = 0
                    OR SUM(CASE WHEN ev.status_id = 3 THEN 1 ELSE 0 END) = COUNT(ev.avaliacao_id)
                ))
                -- Concluída
                OR (:statusId = 2 AND
                    COUNT(ev.avaliacao_id) > 0
                    AND SUM(CASE WHEN ev.status_id = 2 THEN 1 ELSE 0 END) = COUNT(ev.avaliacao_id)
                )
                -- Em andamento (não é nem "não iniciada" nem "concluída")
                OR (:statusId = 1 AND NOT (
                        (COUNT(ev.avaliacao_id) = 0
                          OR SUM(CASE WHEN ev.status_id = 3 THEN 1 ELSE 0 END) = COUNT(ev.avaliacao_id))
                        OR (COUNT(ev.avaliacao_id) > 0
                          AND SUM(CASE WHEN ev.status_id = 2 THEN 1 ELSE 0 END) = COUNT(ev.avaliacao_id))
                ))
          )
        """, nativeQuery = true)
    List<Evaluation> filterByDescriptionCreatorAndComputedStatus(
            @Param("description") String description,
            @Param("creatorId") Long creatorId,
            @Param("statusId") Long statusId
    );

}
