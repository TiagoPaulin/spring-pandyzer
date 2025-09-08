package com.pandyzer.backend.services;

import com.pandyzer.backend.models.User;
import com.pandyzer.backend.models.UserType;
import com.pandyzer.backend.repositories.UserRepository;
import com.pandyzer.backend.repositories.UserTypeRepository;
import com.pandyzer.backend.services.exceptions.BadRequestException;
import com.pandyzer.backend.services.exceptions.ResourceNotFoundException;
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

    // ========= Leitura =========
    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", email));
    }

    public Optional<List<User>> findByUserTypeId(Long userTypeId) {
        return repository.findByUserTypeId(userTypeId);
    }

    public Optional<List<User>> findAvaliadores() {
        return findByUserTypeId(1L);
    }

    // ========= Escrita =========
    public User insert(User obj) {
        validateForInsert(obj);
        obj.setUserType(fetchFullUserType(obj));
        return repository.save(obj);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public User update(Long id, User incoming) {
        // carrega o existente
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));

        // aplica APENAS o que chegou
        if (!isNullOrEmptyOrBlank(incoming.getName())) {
            user.setName(incoming.getName());
        }
        if (!isNullOrEmptyOrBlank(incoming.getEmail())) {
            user.setEmail(incoming.getEmail());
        }

        // senha: só valida/atualiza se veio preenchida
        if (!isNullOrEmptyOrBlank(incoming.getPassword())) {
            if (incoming.getPassword().length() < 6) {
                throw new BadRequestException("A senha deve possuir pelo menos 6 caracteres.");
            }
            user.setPassword(incoming.getPassword());
        }

        // userType: só troca se veio com id
        if (incoming.getUserType() != null && incoming.getUserType().getId() != null) {
            user.setUserType(fetchFullUserType(incoming));
        }

        // garante que os campos essenciais continuam válidos
        validateForUpdate(user);

        return repository.save(user);
    }

    // ========= Validações =========
    private void validateForInsert(User user) {
        if (isNullOrEmptyOrBlank(user.getName())) {
            throw new BadRequestException("É necessário informar o nome de usuário.");
        }
        if (user.getName().trim().length() < 2) {
            throw new BadRequestException("O nome informado deve possuir pelo menos 2 caracteres.");
        }
        if (isNullOrEmptyOrBlank(user.getEmail()) || !user.getEmail().contains("@")) {
            throw new BadRequestException("É necessário informar um email válido.");
        }
        if (isNullOrEmptyOrBlank(user.getPassword()) || user.getPassword().length() < 6) {
            throw new BadRequestException("A senha deve possuir pelo menos 6 caracteres.");
        }
        if (user.getUserType() == null || user.getUserType().getId() == null) {
            throw new BadRequestException("É necessário informar o tipo de usuário.");
        }
    }

    private void validateForUpdate(User user) {
        // No update, senha pode ficar como está (não obrigatória no payload)
        if (isNullOrEmptyOrBlank(user.getName())) {
            throw new BadRequestException("É necessário informar o nome de usuário.");
        }
        if (user.getName().trim().length() < 2) {
            throw new BadRequestException("O nome informado deve possuir pelo menos 2 caracteres.");
        }
        if (isNullOrEmptyOrBlank(user.getEmail()) || !user.getEmail().contains("@")) {
            throw new BadRequestException("É necessário informar um email válido.");
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
        return userTypeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Tipo de usuário com ID " + id + " não encontrado"));
    }
}
