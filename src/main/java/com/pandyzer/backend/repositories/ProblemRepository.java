package com.pandyzer.backend.repositories;

import com.pandyzer.backend.models.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByObjective_Id(Long objectiveId);

    List<Problem> findByObjective_IdAndUser_Id(Long objectiveId, Long userId);

}
