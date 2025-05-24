package com.pandyzer.backend.repositories;

import com.pandyzer.backend.models.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {



}
