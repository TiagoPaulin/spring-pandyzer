package com.pandyzer.backend.config;

import com.pandyzer.backend.models.Heuristic;
import com.pandyzer.backend.models.Severity;
import com.pandyzer.backend.models.User;
import com.pandyzer.backend.models.UserType;
import com.pandyzer.backend.repositories.HeuristicRepository;
import com.pandyzer.backend.repositories.SeverityRepository;
import com.pandyzer.backend.repositories.UserRepository;
import com.pandyzer.backend.repositories.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.Date;

@Configuration
@Profile({"test", "local"})
public class InitialData implements CommandLineRunner {

    @Autowired
    SeverityRepository severityRepository;

    @Autowired
    HeuristicRepository heuristicRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        UserType type1 = new UserType(null, "Avaliador");
        UserType type2 = new UserType(null, "Gerente de Produto");
        UserType type3 = new UserType(null, "Admin");

        userTypeRepository.saveAll(Arrays.asList(type1, type2, type3));

        Severity severity1 = new Severity(null, "Problema Cosmético", 1);
        Severity severity2 = new Severity(null, "Problema Simples", 2);
        Severity severity3 = new Severity(null, "Problema Grave", 3);
        Severity severity4 = new Severity(null, "Problema Catastrófico", 4);

        severityRepository.saveAll(Arrays.asList(severity1, severity2, severity3, severity4));

        Heuristic heuristic1 = new Heuristic(null, "Visibilidade e estado do sistema", new Date());
        Heuristic heuristic2 = new Heuristic(null, "Correspondência entre o sistema e o mundo real", new Date());
        Heuristic heuristic3 = new Heuristic(null, "Controle e liberdade do usuário", new Date());
        Heuristic heuristic4 = new Heuristic(null, "Consistência e padrões", new Date());
        Heuristic heuristic5 = new Heuristic(null, "Prevenção de erro", new Date());
        Heuristic heuristic6 = new Heuristic(null, "Reconhecimento em vez de lembrança", new Date());
        Heuristic heuristic7 = new Heuristic(null, "Flexibilidade e eficiência de uso", new Date());
        Heuristic heuristic8 = new Heuristic(null, "Estética e design minimalista", new Date());
        Heuristic heuristic9 = new Heuristic(null, "Ajudar usuários a reconhecer, diagnosticar e recuperar-se de erros", new Date());
        Heuristic heuristic10 = new Heuristic(null, "Ajuda e documentação", new Date());

        heuristicRepository.saveAll(Arrays.asList(heuristic1, heuristic2, heuristic3, heuristic4, heuristic5, heuristic6, heuristic7, heuristic8, heuristic9, heuristic10));

        User admin = new User(null, "admin", "admin@pandyzer.com", "admin123", 1, type3, new java.sql.Date(new Date().getTime()));

        userRepository.save(admin);

    }

}
