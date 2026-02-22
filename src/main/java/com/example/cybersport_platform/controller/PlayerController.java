package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.PlayerRequest;
import com.example.cybersport_platform.dto.response.PlayerResponse;
import com.example.cybersport_platform.service.PlayerService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService service;

    @PostMapping
    @Operation(summary = "Create player")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Player created"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<PlayerResponse> create(@RequestBody PlayerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update player")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player updated"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<PlayerResponse> update(@PathVariable Long id, @RequestBody PlayerRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get player by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player found"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<PlayerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all players")
    @ApiResponse(responseCode = "200", description = "Players returned")
    public ResponseEntity<List<PlayerResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/search")
    @Operation(summary = "Search players by nickname")
    @ApiResponse(responseCode = "200", description = "Players returned")
    public ResponseEntity<List<PlayerResponse>> searchByNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(service.searchByNickname(nickname));
    }

    @GetMapping("/team/{teamId}")
    @Operation(summary = "Get players by team id")
    @ApiResponse(responseCode = "200", description = "Players returned")
    public ResponseEntity<List<PlayerResponse>> getByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(service.getByTeamId(teamId));
    }

    @GetMapping("/game/{gameId}")
    @Operation(summary = "Get players by game id")
    @ApiResponse(responseCode = "200", description = "Players returned")
    public ResponseEntity<List<PlayerResponse>> getByGameId(@PathVariable Long gameId) {
        return ResponseEntity.ok(service.getByGameId(gameId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete player")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Player deleted"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
