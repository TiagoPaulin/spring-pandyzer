package com.pandyzer.backend.services;

import com.pandyzer.backend.models.Evaluation;
import com.pandyzer.backend.repositories.EvaluationRepository;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository repository;

    public List<Evaluation> findAll() {
        return repository.findAll();
    }

    public Evaluation findById(Long id) {
        Optional<Evaluation> opt = repository.findById(id);
        return opt.orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));
    }

    public List<Evaluation> findCommunityEvaluations(Long userId) {
        return repository.findByIsPublicTrueAndUser_IdNot(userId);
    }

    public long countEvaluations(Long id) {
        return repository.countByUserId(id);
    }

    public List<Evaluation> getByCreator(Integer userId) {
        return repository.findByCreatorId(userId.longValue());
    }

    public Evaluation insert(Evaluation obj) {
        return repository.save(obj);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public Evaluation update(Long id, Evaluation obj) {
        Evaluation entity = findById(id);
        // patch/merge conforme seu modelo
        entity.setDescription(obj.getDescription());
        entity.setStartDate(obj.getStartDate());
        entity.setFinalDate(obj.getFinalDate());
        entity.setPublic(obj.getPublic());
        entity.setEvaluatorsLimit(obj.getEvaluatorsLimit());
        entity.setUser(obj.getUser());
        entity.setApplicationType(obj.getApplicationType());
        return repository.save(entity);
    }

    /**
     * Filtro por título, status GERAL e criador.
     * startDate/finalDate são mantidos na assinatura por compatibilidade,
     * mas não são usados aqui (sua UI atual não envia datas).
     */
    public List<Evaluation> filterEvaluations(
            String description,
            Date startDate,
            Date finalDate,
            Long statusId,
            Long creatorId
    ) {
        String desc = (description == null || description.trim().isEmpty())
                ? null
                : description.trim().toLowerCase();

        return repository.filterByDescriptionCreatorAndComputedStatus(
                desc,
                creatorId,
                statusId
        );
    }

    // EvaluationService.java
    public List<Evaluation> getByEvaluatorUser(Long userId) {
        return repository.findByEvaluatorUserId(userId);
    }

}
