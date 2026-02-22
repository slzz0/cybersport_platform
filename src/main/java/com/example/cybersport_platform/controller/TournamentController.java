package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.TournamentRequest;
import com.example.cybersport_platform.dto.response.TournamentResponse;
import com.example.cybersport_platform.service.TournamentService;
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
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService service;

    @PostMapping
    @Operation(summary = "Create tournament")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tournament created"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<TournamentResponse> create(@RequestBody TournamentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update tournament")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tournament updated"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    public ResponseEntity<TournamentResponse> update(@PathVariable Long id, @RequestBody TournamentRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tournament by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tournament found"),
        @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    public ResponseEntity<TournamentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all tournaments")
    @ApiResponse(responseCode = "200", description = "Tournaments returned")
    public ResponseEntity<List<TournamentResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/game/{gameId}")
    @Operation(summary = "Get tournaments by game id")
    @ApiResponse(responseCode = "200", description = "Tournaments returned")
    public ResponseEntity<List<TournamentResponse>> getByGameId(@PathVariable Long gameId) {
        return ResponseEntity.ok(service.getByGameId(gameId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tournament")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tournament deleted"),
        @ApiResponse(responseCode = "404", description = "Tournament not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
