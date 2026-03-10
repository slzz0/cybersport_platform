package com.example.cybersport_platform.service;

import com.example.cybersport_platform.dto.request.GameWithTeamsRequest;

public interface GameWithTeamsService {
    void saveGameWithTeamsNonTransactional(GameWithTeamsRequest request);
    void saveGameWithTeamsTransactional(GameWithTeamsRequest request);
}
