package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.PlayerRequest;
import com.example.cybersport_platform.dto.response.PlayerResponse;
import com.example.cybersport_platform.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService service;

    @PostMapping
    public ResponseEntity<PlayerResponse> create(@RequestBody PlayerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponse> update(@PathVariable Long id, @RequestBody PlayerRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlayerResponse>> searchByNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(service.searchByNickname(nickname));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<PlayerResponse>> getByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(service.getByTeamId(teamId));
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<PlayerResponse>> getByGameId(@PathVariable Long gameId) {
        return ResponseEntity.ok(service.getByGameId(gameId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
