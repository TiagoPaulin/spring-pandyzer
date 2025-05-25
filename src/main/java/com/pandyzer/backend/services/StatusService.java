package com.pandyzer.backend.services;

import com.pandyzer.backend.models.Status;
import com.pandyzer.backend.repositories.StatusRepository;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusService {

    @Autowired
    private StatusRepository repository;

    public Status findById (Long id) {

        Optional<Status> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));

    }

    public Status insert (Status obj) {

        validate(obj);
        return repository.save(obj);

    }

    public void delete (Long id) {

        repository.deleteById(id);

    }

    @Transactional
    public Status update (Long id, Status obj) {

        validate(obj);
        Status status = repository.getReferenceById(id);
        updateData(status, obj);
        return repository.save(status);

    }

    private void updateData(Status status, Status obj) {

        status.setDescription(obj.getDescription());

    }

    private void validate(Status status) {

        if (isNullOrEmptyOrBlank(status.getDescription())) {
            throw new BadRequestException("É necessário informar uma descrição para o status");
        }

    }

    private boolean isNullOrEmptyOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }


}
