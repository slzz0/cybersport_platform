package com.example.cybersport_platform.repository;

import com.example.cybersport_platform.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "team")
    @Override
    List<Player> findAll();

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = "team")
    List<Player> findByTeamId(Long teamId);

    @Query("SELECT p FROM Player p LEFT JOIN FETCH p.team WHERE p.team.game.id = :gameId")
    List<Player> findByTeamGameIdWithTeam(Long gameId);

    List<Player> findByNicknameContainingIgnoreCase(String nickname);

}
