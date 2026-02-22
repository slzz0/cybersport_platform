package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.MatchRequest;
import com.example.cybersport_platform.dto.response.MatchResponse;
import com.example.cybersport_platform.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create match")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Match created"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<MatchResponse> create(@RequestBody MatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update match")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match updated"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Match not found")
    })
    public ResponseEntity<MatchResponse> update(@PathVariable Long id, @RequestBody MatchRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get match by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match found"),
        @ApiResponse(responseCode = "404", description = "Match not found")
    })
    public ResponseEntity<MatchResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all matches")
    @ApiResponse(responseCode = "200", description = "Matches returned")
    public ResponseEntity<List<MatchResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "Get matches by tournament id")
    @ApiResponse(responseCode = "200", description = "Matches returned")
    public ResponseEntity<List<MatchResponse>> getByTournamentId(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(service.getByTournamentId(tournamentId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete match")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Match deleted"),
        @ApiResponse(responseCode = "404", description = "Match not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
