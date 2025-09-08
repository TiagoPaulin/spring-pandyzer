package com.pandyzer.backend.repositories;

import com.pandyzer.backend.models.Heuristic;
import com.pandyzer.backend.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    /**
     * Busca todos os logs de avaliações que foram CRIADAS por um usuário específico.
     * A busca é feita através da relação Log -> Evaluation -> User.
     * Os resultados são ordenados pela data e hora do log, do mais recente para o mais antigo.
     */
    List<Log> findByEvaluation_User_IdOrderByLogTimestampDesc(Long userId);
}
