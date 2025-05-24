package com.pandyzer.backend.services;

import com.pandyzer.backend.models.Problem;
import com.pandyzer.backend.repositories.ProblemRepository;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProblemService {

    @Autowired
    private ProblemRepository repository;

    public Problem findById (Long id) {

        Optional<Problem> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Problema", id));

    }

    public Problem insert (Problem obj) {

        validate(obj);
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
        problem.setHeuristicId(obj.getHeuristicId());
        problem.setSeverityId(obj.getSeverityId());

    }

    private void validate(Problem problem) {

        if (isNullOrEmptyOrBlank(problem.getDescription())) {
            throw new BadRequestException("É necessário informar a descrição do problema.");
        }
        if (isNullOrEmptyOrBlank(problem.getRecomendation())) {
            throw new BadRequestException("É necessário sugerir uma recomendação para solucionar o problema identioficado.");
        }
        if (problem.getHeuristicId() == null || problem.getHeuristicId() <= 0) {
            throw new BadRequestException("É preciso informar qual heurística estã sendo violada.");
        }
        if (problem.getSeverityId() == null || problem.getSeverityId() <= 0) {
            throw new BadRequestException("É preciso o nível de severidade do problema.");
        }

    }

    private boolean isNullOrEmptyOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }


}
