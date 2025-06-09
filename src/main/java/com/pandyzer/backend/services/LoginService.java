package com.pandyzer.backend.services;

import com.pandyzer.backend.models.User;
import com.pandyzer.backend.models.dto.LoginDTO;
import com.pandyzer.backend.repositories.UserRepository;
import com.pandyzer.backend.services.exceptions.login.EmailNaoEncontradoException;
import com.pandyzer.backend.services.exceptions.login.SenhaIncorretaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    public User autenticar(LoginDTO dadosLogin){
        Optional<User> usuarioOptional = userRepository.findByEmail(dadosLogin.getEmail());

        if (usuarioOptional.isEmpty()){
            throw new EmailNaoEncontradoException("Email não cadastrado: " + dadosLogin.getEmail());
        }

        // **ALERTA DE SEGURANÇA:** O ideal aqui é usar um PasswordEncoder.
        // Exemplo com PasswordEncoder (assumindo que a senha no banco está HASHED):
        // if (!passwordEncoder.matches(dadosLogin.getSenha(), usuario.getPassword())) {
        //    throw new SenhaIncorretaException("Senha incorreta.");
        // }

        if (!dadosLogin.getSenha().equals(usuarioOptional.get().getPassword())) {
            throw new SenhaIncorretaException("Senha incorreta.");
        }

        if (usuarioOptional.get().getActive() == null || usuarioOptional.get().getActive() != 1) {
            throw new SenhaIncorretaException("Usuário inativo ou status desconhecido.");
        }

        return usuarioOptional.get();
    }

}
