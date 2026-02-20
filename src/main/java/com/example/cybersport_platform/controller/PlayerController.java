package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.PlayerDto;
import com.example.cybersport_platform.mapper.PlayerMapper;
import com.example.cybersport_platform.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<PlayerDto> getAllPlayers() {
        return playerService.getAllPlayers()
                .stream()
                .map(PlayerMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id)
                .map(PlayerMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<PlayerDto> searchByNickname(@RequestParam String nickname) {
        return playerService.searchByNickname(nickname)
                .stream()
                .map(PlayerMapper::toDto)
                .toList();
    }

    @GetMapping("/team")
    public List<PlayerDto> searchByTeam(@RequestParam String team) {
        return playerService.searchByTeam(team)
                .stream()
                .map(PlayerMapper::toDto)
                .toList();
    }

    @GetMapping("/game")
    public List<PlayerDto> searchByGame(@RequestParam String game) {
        return playerService.searchByGame(game)
                .stream()
                .map(PlayerMapper::toDto)
                .toList();
    }
}
