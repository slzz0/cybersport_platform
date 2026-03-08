package com.example.cybersport_platform.repository;

import com.example.cybersport_platform.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @EntityGraph(attributePaths = "game")
    @Override
    List<Team> findAll();

    @EntityGraph(attributePaths = "game")
    List<Team> findByGameId(Long gameId);

    @EntityGraph(attributePaths = "game")
    List<Team> findByNameContainingIgnoreCase(String name);
}
