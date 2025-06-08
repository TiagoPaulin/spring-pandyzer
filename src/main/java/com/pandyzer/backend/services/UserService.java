package com.pandyzer.backend.services;

import com.pandyzer.backend.models.User;
import com.pandyzer.backend.models.UserType;
import com.pandyzer.backend.repositories.UserRepository;
import com.pandyzer.backend.repositories.UserTypeRepository;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private UserTypeRepository userTypeRepository;

    public List<User> findAll () {

        return repository.findAll();

    }

    public User findById (Long id) {

        Optional<User> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

    }

    public User findByEmail (String email) {

        Optional<User> obj = repository.findByEmail(email);

        return obj.orElseThrow(() -> new ResourceNotFoundException("Usuário", email));

    }

    public Optional<List<User>> findAvaliadores () {
        return repository.findByUserTypeId(Long.valueOf(1));
    }

    public User insert (User obj) {

        validate(obj);
        obj.setUserType(fetchFullUserType(obj));
        return repository.save(obj);

    }

    public void delete (Long id) {

        repository.deleteById(id);

    }

    @Transactional
    public User update (Long id, User obj) {

        validate(obj);
        User user = repository.getReferenceById(id);
        updateData(user, obj);
        return repository.save(user);

    }

    private void updateData(User user, User obj) {

        user.setName(obj.getName());
        user.setEmail(obj.getEmail());
        user.setPassword(obj.getPassword());

    }

    private void validate(User user) {

        if (isNullOrEmptyOrBlank(user.getName())) {
            throw new BadRequestException("É necessário informar o nome de usuário.");
        }
        if (!(user.getName().length() >= 2)) {
            throw new BadRequestException("O nome informado deve possuir pelo menos 2 caracteres");
        }
        if (isNullOrEmptyOrBlank(user.getEmail()) || !user.getEmail().contains("@")) {
            throw new BadRequestException("É necessário informar um email válido.");
        }
        if (isNullOrEmptyOrBlank(user.getPassword()) || user.getPassword().length() < 6) {
            throw new BadRequestException("A senha deve possuir pelo menos 6 caracteres.");
        }
        if (user.getUserType() == null) {
            throw new BadRequestException("É necessário informar o tipo de usuário.");
        }

    }

    private boolean isNullOrEmptyOrBlank(String value) {

        return value == null || value.trim().isEmpty();

    }

    private UserType fetchFullUserType(User obj) {
        Long id = obj.getUserType().getId();
        return  userTypeRepository.findById(id).orElseThrow(() -> new BadRequestException("Tupo de usuário com ID " + id + " não encontrado"));
    }

}
