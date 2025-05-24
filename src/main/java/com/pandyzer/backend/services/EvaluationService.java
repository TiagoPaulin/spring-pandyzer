package com.pandyzer.backend.services;

import com.pandyzer.backend.models.Evaluation;
import com.pandyzer.backend.repositories.EvaluationRepository;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository repository;

    public Evaluation findById (Long id) {

        Optional<Evaluation> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));

    }

    public Evaluation insert (Evaluation obj) {

        validate(obj);
        return repository.save(obj);

    }

    public void delete (Long id) {

        repository.deleteById(id);

    }

    @Transactional
    public Evaluation update (Long id, Evaluation obj) {

        validate(obj);
        Evaluation evaluation = repository.getReferenceById(id);
        updateData(evaluation, obj);
        return repository.save(evaluation);

    }

    private void updateData(Evaluation evaluation, Evaluation obj) {

        evaluation.setDescription(obj.getDescription());
        evaluation.setStartDate(obj.getStartDate());
        evaluation.setFinalDate(obj.getFinalDate());
        evaluation.setLink(obj.getLink());
        evaluation.setApplicationType(obj.getApplicationType());
        evaluation.setStatusId(obj.getStatusId());

    }

    private void validate(Evaluation evaluation) {

        if (isNullOrEmptyOrBlank(evaluation.getDescription())) {
            throw new BadRequestException("É necessário fornecer uma descrição para a avaliação.");
        }
        if (isNullOrEmptyOrBlank(evaluation.getLink())) {
            throw new BadRequestException("É necessário informar de acesso a interface.");
        }
        if (evaluation.getStartDate() == null) {
            throw new BadRequestException("É necessário informar a data inicial da avaliação.");
        }
        if (evaluation.getFinalDate() == null) {
            throw new BadRequestException("É necessário informar a data final da avaliação.");
        }
        if (evaluation.getFinalDate().before(evaluation.getStartDate())) {
            throw new BadRequestException("A data final não pode ser anterior à data inicial.");
        }
        if (evaluation.getApplicationType() == null || evaluation.getApplicationType() <= 0) {
            throw new BadRequestException("É necessário informar um tipo de aplicação válido.");
        }

    }

    private boolean isNullOrEmptyOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }

}