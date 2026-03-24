package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.PlayerRequest;
import com.example.cybersport_platform.dto.response.PlayerResponse;
import com.example.cybersport_platform.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
@Tag(name = "Players", description = "Operations with players")
public class PlayerController {

    private final PlayerService service;

    @PostMapping
    @Operation(summary = "Create player")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Player created"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<PlayerResponse> create(@Valid @RequestBody PlayerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PostMapping("/bulk/transactional")
    @Operation(summary = "Create players in bulk with transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Players created"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public ResponseEntity<List<PlayerResponse>> createBulkTransactional(
            @Valid @NotEmpty @RequestBody List<@Valid PlayerRequest> requests
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBulkTransactional(requests));
    }

    @PostMapping("/bulk/non-transactional")
    @Operation(summary = "Create players in bulk without transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Players created"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public ResponseEntity<List<PlayerResponse>> createBulkNonTransactional(
            @Valid @NotEmpty @RequestBody List<@Valid PlayerRequest> requests
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createBulkNonTransactional(requests));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update player")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player updated"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<PlayerResponse> update(@PathVariable Long id, @Valid @RequestBody PlayerRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get player by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player received"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    public ResponseEntity<PlayerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all players")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Players received")
    })
    public ResponseEntity<List<PlayerResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/search")
    @Operation(summary = "Search players by nickname")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Players received")
    })
    public ResponseEntity<List<PlayerResponse>> searchByNickname(@RequestParam String nickname) {
        return ResponseEntity.ok(service.searchByNickname(nickname));
    }

    @GetMapping("/team/{teamId}")
    @Operation(summary = "Get players by team id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Players received"),
        @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public ResponseEntity<List<PlayerResponse>> getByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(service.getByTeamId(teamId));
    }

    @GetMapping("/game/{gameId}")
    @Operation(summary = "Get players by game id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Players received"),
        @ApiResponse(responseCode = "404", description = "Game not found")
    })
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
