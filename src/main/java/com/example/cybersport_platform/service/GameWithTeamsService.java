package com.example.cybersport_platform.service;

import com.example.cybersport_platform.dto.request.GameWithTeamsRequest;

public interface GameWithTeamsService {
    void saveGameWithTeamsAndPlayersNonTransactional(GameWithTeamsRequest request);
    void saveGameWithTeamsAndPlayersTransactional(GameWithTeamsRequest request);
}
