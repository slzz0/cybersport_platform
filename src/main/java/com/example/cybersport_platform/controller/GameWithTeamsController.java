package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.GameWithTeamsRequest;
import com.example.cybersport_platform.service.GameWithTeamsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/demo/game-with-teams")
public class GameWithTeamsController {

    private final GameWithTeamsService gameWithTeamsService;

    @PostMapping("/non-transactional")
    @Operation(summary = "Сохранение без @Transactional")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успех"),
        @ApiResponse(responseCode = "500", description = "При simulateError: partial save (game+2 teams)")
    })
    public ResponseEntity<Void> saveNonTransactional(@RequestBody GameWithTeamsRequest request) {
        gameWithTeamsService.saveGameWithTeamsAndPlayersNonTransactional(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transactional")
    @Operation(summary = "Сохранение с @Transactional (демо отката)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успех"),
        @ApiResponse(responseCode = "500", description = "При simulateError: full rollback")
    })
    public ResponseEntity<Void> saveTransactional(@RequestBody GameWithTeamsRequest request) {
        gameWithTeamsService.saveGameWithTeamsAndPlayersTransactional(request);
        return ResponseEntity.ok().build();
    }
}
