package com.pandyzer.backend.controllers;

import com.pandyzer.backend.models.User;
import com.pandyzer.backend.models.dto.LoginDTO;
import com.pandyzer.backend.services.LoginService;
import com.pandyzer.backend.services.exceptions.login.EmailNaoEncontradoException;
import com.pandyzer.backend.services.exceptions.login.SenhaIncorretaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO dados) {
        try {
            User usuarioAutenticado = loginService.autenticar(dados);
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (EmailNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SenhaIncorretaException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro inesperado durante o login.");
        }
    }
}
