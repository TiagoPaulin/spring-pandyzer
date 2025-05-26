package com.pandyzer.backend.services;

import com.pandyzer.backend.models.Heuristic;
import com.pandyzer.backend.models.UserType;
import com.pandyzer.backend.repositories.HeuristicRepository;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeuristicService {

    @Autowired
    private HeuristicRepository repository;

    public List<Heuristic> findAll () {

        return repository.findAll();

    }

    public Heuristic findById (Long id) {

        Optional<Heuristic> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Heuristica", id));

    }

    public Heuristic insert (Heuristic obj) {

        validate(obj);
        return repository.save(obj);

    }

    public void delete (Long id) {

        repository.deleteById(id);

    }

    @Transactional
    public Heuristic update (Long id, Heuristic obj) {

        validate(obj);
        Heuristic heuristic = repository.getReferenceById(id);
        updateData(heuristic, obj);
        return repository.save(heuristic);

    }

    private void updateData(Heuristic heuristic, Heuristic obj) {

        heuristic.setDescription(obj.getDescription());

    }

    private void validate(Heuristic heuristic) {

        if (isNullOrEmptyOrBlank(heuristic.getDescription())) {
            throw new BadRequestException("Informe uma descrição para a heurística.");
        }

    }

    private boolean isNullOrEmptyOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }


}
