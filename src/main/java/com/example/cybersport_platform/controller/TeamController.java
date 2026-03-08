package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.TeamRequest;
import com.example.cybersport_platform.dto.response.TeamResponse;
import com.example.cybersport_platform.service.TeamService;
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
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService service;

    @PostMapping
    public ResponseEntity<TeamResponse> create(@RequestBody TeamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> update(@PathVariable Long id, @RequestBody TeamRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<TeamResponse>> getByGameId(@PathVariable Long gameId) {
        return ResponseEntity.ok(service.getByGameId(gameId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
