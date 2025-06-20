package com.pandyzer.backend.services;

import com.pandyzer.backend.models.*;
import com.pandyzer.backend.repositories.EvaluationRepository;
import com.pandyzer.backend.repositories.EvaluatorRepository;
import com.pandyzer.backend.repositories.StatusRepository;
import com.pandyzer.backend.repositories.UserRepository;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluatorService {

    @Autowired
    private EvaluatorRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private StatusRepository statusRepository;

    public List<Evaluator> findAll () {

        return repository.findAll();

    }

    public Evaluator findById (Long id) {

        Optional<Evaluator> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Avaliador", id));

    }

    public List<Evaluator> findByEvaluationId (Long evaluationId) {
        return repository.findByEvaluationId(evaluationId);
    }

    public Evaluator insert (Evaluator obj) {

        validate(obj);
        obj.setUser(fetchFullUser(obj));
        obj.setEvaluation(fetchFullEvaluation(obj));
        obj.setStatus(fetchFullStatus(obj));
        return repository.save(obj);

    }

    public void delete (Long id) {

        repository.deleteById(id);

    }

    @Transactional
    public Evaluator update (Long id, Evaluator obj) {

        validate(obj);
        Evaluator evaluator = repository.getReferenceById(id);
        updateData(evaluator, obj);
        return repository.save(evaluator);

    }

    private void updateData(Evaluator evaluator, Evaluator obj) {

        evaluator.setUser(fetchFullUser(obj));
        evaluator.setEvaluation(fetchFullEvaluation(obj));
        evaluator.setStatus(fetchFullStatus(obj));

    }

    public Evaluator updateStatusEvaluator(Long idUser, Long idEvaluation, Long idStatus){
        Evaluator evaluator = repository.findByUserIdAndEvaluationId(idUser, idEvaluation);
        if (evaluator == null) {
            throw new ResourceNotFoundException("Avaliador não encontrado para os IDs fornecidos.", idUser);
        }

        Status status = statusRepository.findById(idStatus)
                .orElseThrow(() -> new ResourceNotFoundException("Status", idStatus));

        evaluator.setStatus(status);
        return repository.save(evaluator);
    }

    private void validate(Evaluator evaluator) {

        if (evaluator.getUser() == null) {
            throw new BadRequestException("É preciso informar um usuário.");
        }
        if (evaluator.getEvaluation() == null) {
            throw new BadRequestException("É preciso informar uma avaliação.");
        }
        if (evaluator.getStatus() == null) {
            throw new BadRequestException("É preciso informar um status para a avaliação do avaliador.");
        }

    }

    private User fetchFullUser(Evaluator obj) {

        Long id = obj.getUser().getId();
        return userRepository.findById(id).orElseThrow(() -> new BadRequestException("Usuário com ID " + id + " não encontrado"));

    }

    private Evaluation fetchFullEvaluation(Evaluator obj) {

        Long id = obj.getEvaluation().getId();
        return evaluationRepository.findById(id).orElseThrow(() -> new BadRequestException("Avaliação com ID " + id + " não encontrada."));

    }

    private Status fetchFullStatus(Evaluator obj) {

        Long id = obj.getStatus().getId();
        return statusRepository.findById(id).orElseThrow(() -> new BadRequestException("Status com ID " + id + " não encontrada."));

    }


}
