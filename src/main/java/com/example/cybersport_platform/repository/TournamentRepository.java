package com.example.cybersport_platform.repository;

import com.example.cybersport_platform.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @EntityGraph(attributePaths = "game")
    @Override
    List<Tournament> findAll();

    @EntityGraph(attributePaths = "game")
    List<Tournament> findByGameId(Long gameId);
}
