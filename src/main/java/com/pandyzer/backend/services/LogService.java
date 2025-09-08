package com.pandyzer.backend.services;

import com.pandyzer.backend.models.Log;
import com.pandyzer.backend.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogService {
    @Autowired
    private LogRepository repository;

    public List<Log> findLogsForUserCreatedEvaluations(Long userId) {
        // A sua observação sobre a ordenação estava um pouco contraditória.
        // "decrescente" e "mais recente por último" são opostos.
        // Implementei a ordenação decrescente (mais recente primeiro), que é o padrão para logs.
        return repository.findByEvaluation_User_IdOrderByLogTimestampDesc(userId);
    }

    public Log insert (Log log) {
        return repository.save(log);
    }

    public Optional<Log> findById(Long id) {
        return repository.findById(id);
    }

    public List<Log> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
