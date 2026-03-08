package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.TournamentRequest;
import com.example.cybersport_platform.dto.response.TournamentResponse;
import com.example.cybersport_platform.service.TournamentService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService service;

    @PostMapping
    public ResponseEntity<TournamentResponse> create(@RequestBody TournamentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TournamentResponse> update(@PathVariable Long id, @RequestBody TournamentRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TournamentResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<TournamentResponse>> getByGameId(@PathVariable Long gameId) {
        return ResponseEntity.ok(service.getByGameId(gameId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
