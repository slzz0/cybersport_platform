package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.MatchRequest;
import com.example.cybersport_platform.dto.response.MatchResponse;
import com.example.cybersport_platform.service.MatchService;
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
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService service;

    @PostMapping
    public ResponseEntity<MatchResponse> create(@RequestBody MatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchResponse> update(@PathVariable Long id, @RequestBody MatchRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MatchResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/tournament/{tournamentId}")
    public ResponseEntity<List<MatchResponse>> getByTournamentId(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(service.getByTournamentId(tournamentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
