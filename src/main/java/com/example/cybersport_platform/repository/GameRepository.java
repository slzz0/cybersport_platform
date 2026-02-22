package com.example.cybersport_platform.repository;

import com.example.cybersport_platform.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
