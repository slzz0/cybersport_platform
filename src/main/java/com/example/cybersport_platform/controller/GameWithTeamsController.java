package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.GameWithTeamsRequest;
import com.example.cybersport_platform.service.GameWithTeamsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/game-with-teams")
@Tag(name = "GameWithTeams", description = "Transactional game/team save demos")
public class GameWithTeamsController {

    private final GameWithTeamsService gameWithTeamsService;

    @PostMapping("/non-transactional")
    @Operation(summary = "Save game with teams without transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Data saved"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<Void> saveNonTransactional(@Valid @RequestBody GameWithTeamsRequest request) {
        gameWithTeamsService.saveGameWithTeamsNonTransactional(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transactional")
    @Operation(summary = "Save game with teams in transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Data saved"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<Void> saveTransactional(@Valid @RequestBody GameWithTeamsRequest request) {
        gameWithTeamsService.saveGameWithTeamsTransactional(request);
        return ResponseEntity.ok().build();
    }
}
