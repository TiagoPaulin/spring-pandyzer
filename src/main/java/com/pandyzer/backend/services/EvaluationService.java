package com.pandyzer.backend.services;

import com.pandyzer.backend.models.ApplicationType;
import com.pandyzer.backend.models.Evaluation;
import com.pandyzer.backend.models.Status;
import com.pandyzer.backend.models.User;
import com.pandyzer.backend.repositories.ApplicationTypeRepository;
import com.pandyzer.backend.repositories.EvaluationRepository;
import com.pandyzer.backend.repositories.StatusRepository;
import com.pandyzer.backend.repositories.UserRepository;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicationTypeRepository applicationTypeRepository;
    @Autowired
    private StatusRepository statusRepository;

    public Evaluation findById (Long id) {

        Optional<Evaluation> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));

    }

    public Evaluation insert (Evaluation obj) {

        validate(obj);
        obj.setUser(fetchFullUser(obj));
        obj.setApplicationType(fetchFullApplicationType(obj));
        obj.setStatus(fetchFullStatus(obj));
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
        evaluation.setApplicationType(fetchFullApplicationType(obj));
        evaluation.setStatus(fetchFullStatus(obj));
        evaluation.setUser(fetchFullUser(obj));

    }

    private void validate(Evaluation evaluation) {

        if (isNullOrEmptyOrBlank(evaluation.getDescription())) {
            throw new BadRequestException("É necessário fornecer uma descrição para a avaliação.");
        }
        if (isNullOrEmptyOrBlank(evaluation.getLink())) {
            throw new BadRequestException("É necessário informar o link de acesso a interface.");
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
        if (evaluation.getApplicationType() == null) {
            throw new BadRequestException("É necessário informar um tipo de aplicação válido.");
        }
        if (evaluation.getUser() == null || evaluation.getUser().getId() == null) {
            throw new BadRequestException("A avaliação deve estar relacionada a um usuário.");
        }
        if (evaluation.getStatus() == null) {
            throw new BadRequestException("Informe um status para a avalição");
        }

    }

    private boolean isNullOrEmptyOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }

    private User fetchFullUser(Evaluation obj) {
        Long id = obj.getUser().getId();
        return userRepository.findById(id).orElseThrow(() -> new BadRequestException("Usuário com ID " + id + " não encontrado."));
    }

    private ApplicationType fetchFullApplicationType(Evaluation obj) {

        Long id = obj.getApplicationType().getId();
        return applicationTypeRepository.findById(id).orElseThrow(() -> new BadRequestException("Tipo de aplicação com ID " + id + " não encontrado."));

    }

    private Status fetchFullStatus(Evaluation obj) {

        Long id = obj.getStatus().getId();
        return statusRepository.findById(id).orElseThrow(() -> new BadRequestException("Status com ID " + id + " não encontrado."));

    }

}