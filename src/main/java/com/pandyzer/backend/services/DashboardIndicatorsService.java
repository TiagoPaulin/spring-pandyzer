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
        // 1. Obter o intervalo de datas para o mês atual
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        // Converter LocalDate para Date
        Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lastDayOfMonth.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        Date currentDate = new Date();

        // 2. Buscar os dados usando os repositories
        Integer createdCount = evaluationRepository.countByUserIdAndRegisterBetween(userId, startDate, endDate);
        Integer completedCount = evaluatorRepository.countCompletedForUserInPeriod(userId, startDate, endDate);
        Integer ongoingCount = evaluatorRepository.countOngoingForUser(userId, currentDate);

        // 3. Montar e retornar o DTO
        DashboardIndicators indicators = new DashboardIndicators();
        indicators.setAvaliacoesCriadas(createdCount != null ? createdCount : 0);
        indicators.setAvaliacoesFeitas(completedCount != null ? completedCount : 0);
        indicators.setAvaliacoesEmAndamento(ongoingCount != null ? ongoingCount : 0);

        return indicators;
    }

}
