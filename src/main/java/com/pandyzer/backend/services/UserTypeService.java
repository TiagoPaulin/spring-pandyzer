package com.pandyzer.backend.services;

import com.pandyzer.backend.models.UserType;
import com.pandyzer.backend.repositories.UserTypeRepository;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

@Service
public class UserTypeService {

    @Autowired
    private UserTypeRepository repository;

    public List<UserType> findAll () {

        return repository.findAll();

    }

    public UserType findById (Long id) {

        Optional<UserType> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Tipo de usuário", id));

    }

    public UserType insert (UserType obj) {

        validate(obj);
        return repository.save(obj);

    }

    public void delete (Long id) {

        repository.deleteById(id);

    }

    @Transactional
    public UserType update (Long id, UserType obj) {

        validate(obj);
        UserType userType = repository.getReferenceById(id);
        updateData(userType, obj);
        return repository.save(userType);

    }

    private void updateData(UserType userType, UserType obj) {

        userType.setDescription(obj.getDescription());

    }

    private void validate(UserType userType) {

        if (isNullOrEmptyOrBlank(userType.getDescription())) {
            throw new BadRequestException("É necessário informar a descrição para o tipo de usuário.");
        }

    }

    private boolean isNullOrEmptyOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }


}
