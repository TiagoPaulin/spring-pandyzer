package com.pandyzer.backend.services;

import com.pandyzer.backend.models.Severity;
import com.pandyzer.backend.repositories.SeverityRepository;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeverityService {

    @Autowired
    private SeverityRepository repository;

    public Severity findById (Long id) {

        Optional<Severity> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Severidade", id));

    }

    public Severity insert (Severity obj) {

        validate(obj);
        return repository.save(obj);

    }

    public void delete (Long id) {

        repository.deleteById(id);

    }

    @Transactional
    public Severity update (Long id, Severity obj) {

        validate(obj);
        Severity severity = repository.getReferenceById(id);
        updateData(severity, obj);
        return repository.save(severity);

    }

    private void updateData(Severity severity, Severity obj) {

        severity.setDescription(obj.getDescription());

    }

    private void validate(Severity severity) {

       if (isNullOrEmptyOrBlank(severity.getDescription())) {
           throw new BadRequestException("É necessário informar uma descrição para o nível de severidade");
       }

    }

    private boolean isNullOrEmptyOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }

}
