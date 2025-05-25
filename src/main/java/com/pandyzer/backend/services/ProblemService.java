package com.pandyzer.backend.services;

import com.pandyzer.backend.models.Heuristic;
import com.pandyzer.backend.models.Problem;
import com.pandyzer.backend.repositories.HeuristicRepository;
import com.pandyzer.backend.repositories.ProblemRepository;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {

    @Autowired
    private ProblemRepository repository;
    @Autowired
    private HeuristicRepository heuristicRepository;

    public Problem findById (Long id) {

        Optional<Problem> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Problema", id));

    }

    public Problem insert (Problem obj) {

        validate(obj);
        obj.setHeuristic(fetchFullHeuristic(obj));
        return repository.save(obj);

    }

    public void delete (Long id) {

        repository.deleteById(id);

    }

    @Transactional
    public Problem update (Long id, Problem obj) {

        validate(obj);
        Problem problem = repository.getReferenceById(id);
        updateData(problem, obj);
        return repository.save(problem);

    }

    private void updateData(Problem problem, Problem obj) {

        problem.setDescription(obj.getDescription());
        problem.setRecomendation(obj.getRecomendation());
        problem.setHeuristic(fetchFullHeuristic(obj));
        problem.setSeverityId(obj.getSeverityId());

    }

    private void validate(Problem problem) {

        if (isNullOrEmptyOrBlank(problem.getDescription())) {
            throw new BadRequestException("É necessário informar a descrição do problema.");
        }
        if (isNullOrEmptyOrBlank(problem.getRecomendation())) {
            throw new BadRequestException("É necessário sugerir uma recomendação para solucionar o problema identificado.");
        }
        if (problem.getHeuristic() == null || problem.getHeuristic().getId() == null) {
            throw new BadRequestException("É preciso informar a heurística que está sendo violada.");
        }
        if (problem.getSeverityId() == null || problem.getSeverityId() <= 0) {
            throw new BadRequestException("É preciso o nível de severidade do problema.");
        }

    }

    private boolean isNullOrEmptyOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }

    private Heuristic fetchFullHeuristic(Problem obj) {
        Long id = obj.getHeuristic().getId();
        return heuristicRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Heurística com ID " + id + " não encontrada."));
    }

}
