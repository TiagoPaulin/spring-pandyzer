package com.pandyzer.backend.services;

import com.pandyzer.backend.models.Problem;
import com.pandyzer.backend.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {

    @Autowired
    private ProblemRepository repository;

    public List<Problem> findAll() {
        return repository.findAll();
    }

    public List<Problem> findByObjectiveId(Long objectiveId) {
        return repository.findByObjective_Id(objectiveId);
    }

    public List<Problem> findByObjectiveIdAndUserId(Long objectiveId, Long userId) {
        return repository.findByObjective_IdAndUser_Id(objectiveId, userId);
    }

    public Problem findById(Long id) {
        Optional<Problem> opt = repository.findById(id);
        return opt.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Problema não encontrado: " + id));
    }

    @Transactional
    public Problem insert(Problem obj) {
        validarObrigatorios(obj);

        if (obj.getRegister() == null) {
            obj.setRegister(new Date());
        }

        // Remove o prefixo data:*;base64, se vier
        obj.setImageBase64(sanitizarBase64(obj.getImageBase64()));

        return repository.save(obj);
    }

    @Transactional
    public Problem update(Long id, Problem obj) {
        Problem entity = findById(id);

        if (obj.getDescription() != null) entity.setDescription(obj.getDescription());
        if (obj.getRecomendation() != null) entity.setRecomendation(obj.getRecomendation());
        if (obj.getRegister() != null) entity.setRegister(obj.getRegister());
        if (obj.getObjective() != null) entity.setObjective(obj.getObjective());
        if (obj.getHeuristic() != null) entity.setHeuristic(obj.getHeuristic());
        if (obj.getSeverity() != null) entity.setSeverity(obj.getSeverity());
        if (obj.getUser() != null) entity.setUser(obj.getUser());
        if (obj.getImageBase64() != null) {
            entity.setImageBase64(sanitizarBase64(obj.getImageBase64()));
        }

        validarObrigatorios(entity);
        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // ---------- helpers ----------

    private void validarObrigatorios(Problem p) {
        if (p.getUser() == null || p.getUser().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O problema deve estar relacionado a um usuário.");
        }
        if (p.getObjective() == null || p.getObjective().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O problema deve estar relacionado a um objetivo.");
        }
        if (p.getHeuristic() == null || p.getHeuristic().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecione uma heurística.");
        }
        if (p.getSeverity() == null || p.getSeverity().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selecione uma severidade.");
        }
        // Opcional: trava de tamanho para evitar payloads exagerados
        if (p.getImageBase64() != null && p.getImageBase64().length() > 5_000_000) { // ~5MB em texto
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagem Base64 muito grande.");
        }
    }

    private String sanitizarBase64(String raw) {
        if (raw == null) return null;
        // Aceita tanto com prefixo "data:image/png;base64," quanto somente o base64
        int idx = raw.indexOf("base64,");
        return (idx >= 0) ? raw.substring(idx + "base64,".length()) : raw;
    }
}
