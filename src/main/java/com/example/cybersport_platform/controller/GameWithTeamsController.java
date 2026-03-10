package com.example.cybersport_platform.controller;

import com.example.cybersport_platform.dto.request.GameWithTeamsRequest;
import com.example.cybersport_platform.service.GameWithTeamsService;
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
    public ResponseEntity<Void> saveNonTransactional(@RequestBody GameWithTeamsRequest request) {
        gameWithTeamsService.saveGameWithTeamsNonTransactional(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transactional")
    public ResponseEntity<Void> saveTransactional(@RequestBody GameWithTeamsRequest request) {
        gameWithTeamsService.saveGameWithTeamsTransactional(request);
        return ResponseEntity.ok().build();
    }
}
