package com.pandyzer.backend.services;

import com.pandyzer.backend.models.dto.DashboardIndicators;
import com.pandyzer.backend.repositories.EvaluationRepository;
import com.pandyzer.backend.repositories.EvaluatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Service
public class DashboardIndicatorsService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private EvaluatorRepository evaluatorRepository;

    public DashboardIndicators getIndicatorsByUserId(Long userId){
        // Busca a contagem para cada status diretamente do repositório
        Integer emAndamento = evaluatorRepository.countByUserIdAndStatusId(userId, 1L); // Status ID 1
        Integer concluidas = evaluatorRepository.countByUserIdAndStatusId(userId, 2L);  // Status ID 2
        Integer naoIniciadas = evaluatorRepository.countByUserIdAndStatusId(userId, 3L); // Status ID 3

        // Monta e retorna o DTO
        DashboardIndicators indicators = new DashboardIndicators();
        indicators.setAvaliacoesEmAndamento(emAndamento);
        indicators.setAvaliacoesConcluidas(concluidas);
        indicators.setAvaliacoesNaoIniciadas(naoIniciadas);

        return indicators;
    }

}
