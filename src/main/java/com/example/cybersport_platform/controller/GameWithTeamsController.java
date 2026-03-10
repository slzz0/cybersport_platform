package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.GameWithTeamsRequest;
import com.example.cybersport_platform.service.GameWithTeamsService;
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
@RequestMapping("/api/game-with-teams")
public class GameWithTeamsController {

    private final GameWithTeamsService gameWithTeamsService;

    @PostMapping("/non-transactional")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Error occurred; game and teams remain saved")
    })
    public ResponseEntity<Void> saveNonTransactional(@RequestBody GameWithTeamsRequest request) {
        gameWithTeamsService.saveGameWithTeamsNonTransactional(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transactional")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "500", description = "Error occurred; full rollback")
    })
    public ResponseEntity<Void> saveTransactional(@RequestBody GameWithTeamsRequest request) {
        gameWithTeamsService.saveGameWithTeamsTransactional(request);
        return ResponseEntity.ok().build();
    }
}
