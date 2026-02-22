package com.example.cybersport_platform.repository;

import com.example.cybersport_platform.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByGameId(Long gameId);

    List<Team> findByNameContainingIgnoreCase(String name);
}
