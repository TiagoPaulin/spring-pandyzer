package com.pandyzer.backend.services;

import com.pandyzer.backend.models.ApplicationType;
import com.pandyzer.backend.repositories.ApplicationTypeRepository;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationTypeService {

    @Autowired
    private ApplicationTypeRepository repository;

    public ApplicationType findById (Long id) {

        Optional<ApplicationType> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Tipo de aplicação", id));

    }

    public ApplicationType insert (ApplicationType obj) {

        validate(obj);
        return repository.save(obj);

    }

    public void delete (Long id) {

        repository.deleteById(id);

    }

    @Transactional
    public ApplicationType update (Long id, ApplicationType obj) {

        validate(obj);
        ApplicationType applicationType = repository.getReferenceById(id);
        updateData(applicationType, obj);
        return repository.save(applicationType);

    }

    private void updateData(ApplicationType applicationType, ApplicationType obj) {

       applicationType.setDescription(obj.getDescription());

    }

    private void validate(ApplicationType applicationType) {

        if (isNullOrEmptyOrBlank(applicationType.getDescription())) {
            throw new BadRequestException("É preciso informar uma descrição para o tipo de aplicação.");
        }

    }

    private boolean isNullOrEmptyOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }


}
