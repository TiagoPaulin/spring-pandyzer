package com.pandyzer.backend.services;

import com.pandyzer.backend.models.Evaluation;
import com.pandyzer.backend.models.Evaluator;
import com.pandyzer.backend.models.User;
import com.pandyzer.backend.models.dto.DashboardIndicators;
import com.pandyzer.backend.repositories.EvaluationRepository;
import com.pandyzer.backend.repositories.EvaluatorRepository;
import com.pandyzer.backend.repositories.UserRepository;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardIndicatorsService {

    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private EvaluatorRepository evaluatorRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true) // readOnly = true é uma otimização para consultas
    public DashboardIndicators getIndicatorsByUserId(Long userId) {
        // 1. Busca o usuário para identificar seu tipo
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", userId));

        String userType = user.getUserType().getDescription();

        // 2. Roteia para o método de cálculo correto com base no tipo de usuário
        if ("Avaliador".equalsIgnoreCase(userType)) {
            return calculateForEvaluator(userId);
        } else {
            // Assume que outros tipos (Gerente de Produto, Cliente) veem o dashboard de criador
            return calculateForCreator(userId);
        }
    }

    /**
     * Calcula os indicadores para um usuário do tipo AVALIADOR.
     * Conta o status das avaliações em que ele está diretamente envolvido.
     */
    private DashboardIndicators calculateForEvaluator(Long userId) {
        Integer naoIniciadas = evaluatorRepository.countByUserIdAndStatusId(userId, 3L);
        Integer emAndamento = evaluatorRepository.countByUserIdAndStatusId(userId, 1L);
        Integer concluidas = evaluatorRepository.countByUserIdAndStatusId(userId, 2L);

        DashboardIndicators indicators = new DashboardIndicators();
        indicators.setAvaliacoesNaoIniciadas(naoIniciadas);
        indicators.setAvaliacoesEmAndamento(emAndamento);
        indicators.setAvaliacoesConcluidas(concluidas);
        return indicators;
    }

    /**
     * Calcula os indicadores para um usuário do tipo CRIADOR (Gerente de Produto/Cliente).
     * Analisa o status geral das avaliações que ele criou.
     */
    private DashboardIndicators calculateForCreator(Long userId) {
        List<Evaluation> userEvaluations = evaluationRepository.findByUser_Id(userId);

        int naoIniciadas = 0;
        int emAndamento = 0;
        int concluidas = 0;

        for (Evaluation evaluation : userEvaluations) {
            List<Evaluator> evaluators = evaluation.getEvaluators();
            if (evaluators.isEmpty()) {
                naoIniciadas++;
                continue;
            }

            // Verifica se TODAS as avaliações dos avaliadores estão concluídas
            boolean allConcluded = evaluators.stream()
                    .allMatch(e -> e.getStatus().getId() == 2L);
            if (allConcluded) {
                concluidas++;
                continue;
            }

            // Verifica se TODAS as avaliações dos avaliadores estão como não iniciadas
            boolean allNotStarted = evaluators.stream()
                    .allMatch(e -> e.getStatus().getId() == 3L);
            if (allNotStarted) {
                naoIniciadas++;
                continue;
            }

            // Se não se encaixa nas regras acima, é porque está em andamento
            emAndamento++;
        }

        DashboardIndicators indicators = new DashboardIndicators();
        indicators.setAvaliacoesNaoIniciadas(naoIniciadas);
        indicators.setAvaliacoesEmAndamento(emAndamento);
        indicators.setAvaliacoesConcluidas(concluidas);
        return indicators;
    }
}