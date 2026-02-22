package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.GameRequest;
import com.example.cybersport_platform.dto.response.GameResponse;
import com.example.cybersport_platform.service.GameService;
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
@RequestMapping("/api/games")
public class GameController {

    private final GameService service;

    @PostMapping
    @Operation(summary = "Create game")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Game created"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<GameResponse> create(@RequestBody GameRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update game")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Game updated"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "Game not found")
    })
    public ResponseEntity<GameResponse> update(@PathVariable Long id, @RequestBody GameRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get game by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Game found"),
        @ApiResponse(responseCode = "404", description = "Game not found")
    })
    public ResponseEntity<GameResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all games")
    @ApiResponse(responseCode = "200", description = "Games returned")
    public ResponseEntity<List<GameResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete game")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Game deleted"),
        @ApiResponse(responseCode = "404", description = "Game not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
