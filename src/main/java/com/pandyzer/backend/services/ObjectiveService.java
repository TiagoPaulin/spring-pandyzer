package com.pandyzer.backend.services;

import com.pandyzer.backend.models.Evaluation;
import com.pandyzer.backend.models.Objective;
import com.pandyzer.backend.models.Status;
import com.pandyzer.backend.repositories.EvaluationRepository;
import com.pandyzer.backend.repositories.ObjectiveRepository;
import com.pandyzer.backend.repositories.StatusRepository;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ObjectiveService {

    @Autowired
    private ObjectiveRepository repository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private StatusRepository statusRepository;

    public Objective findById (Long id) {

        Optional<Objective> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Objetivo", id));

    }

    public Objective insert (Objective obj) {

        validate(obj);
        obj.setEvaluation(fetchFullEvaluation(obj));
        obj.setStatus(fetchFullStatus(obj));
        return repository.save(obj);

    }

    public void delete (Long id) {

        repository.deleteById(id);

    }

    @Transactional
    public Objective update (Long id, Objective obj) {

        validate(obj);
        Objective objective = repository.getReferenceById(id);
        updateData(objective, obj);
        return repository.save(objective);

    }

    private void updateData(Objective objective, Objective obj) {

        objective.setDescription(obj.getDescription());
        objective.setEvaluation(fetchFullEvaluation(obj));
        objective.setStatus(fetchFullStatus(obj));

    }

    private void validate(Objective objective) {

        if (isNullOrEmptyOrBlank(objective.getDescription())) {
            throw new BadRequestException("É necessário informar a descrição do objetivo.");
        }
        if (objective.getEvaluation() == null || objective.getEvaluation().getId() == null) {
            throw new BadRequestException("O objetivo deve estar relacionado a uma avaliação.");
        }
        if (objective.getStatus() == null) {
            throw new BadRequestException("É necessário informar um status para o objetivo");
        }

    }

    private boolean isNullOrEmptyOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }

    private Evaluation fetchFullEvaluation(Objective obj) {
        Long id = obj.getEvaluation().getId();
        return evaluationRepository.findById(id).orElseThrow(() -> new BadRequestException("Avaliação com ID " + id + " não encontrada."));
    }

    private Status fetchFullStatus(Objective obj) {

        Long id = obj.getStatus().getId();
        return statusRepository.findById(id).orElseThrow(() -> new BadRequestException("Status com ID " + id + " não encontrada."));

    }

}
