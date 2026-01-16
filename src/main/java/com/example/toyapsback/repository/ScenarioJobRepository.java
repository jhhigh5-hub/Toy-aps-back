package com.example.toyapsback.repository;

import com.example.toyapsback.entity.ScenarioJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioJobRepository extends JpaRepository<ScenarioJob, Long> {
}
