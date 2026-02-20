package com.example.cybersport_platform.service;

import com.example.cybersport_platform.model.Player;
import com.example.cybersport_platform.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return playerRepository.findById(id);
    }

    public List<Player> searchByNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            return Collections.emptyList();
        }
        return playerRepository.findByNicknameContaining(nickname);
    }

    public List<Player> searchByTeam(String team) {
        if (team == null || team.isBlank()) {
            return Collections.emptyList();
        }
        return playerRepository.findByTeam(team);
    }

    public List<Player> searchByGame(String game) {
        if(game == null || game.isBlank()) {
            return Collections.emptyList();
        }
        return playerRepository.findByGame(game);
    }
}
