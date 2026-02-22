package com.example.cybersport_platform.service;

import com.example.cybersport_platform.dto.request.GameRequest;
import com.example.cybersport_platform.dto.response.GameResponse;

import java.util.List;

public interface GameService {
    GameResponse create(GameRequest request);
    GameResponse update(Long id, GameRequest request);
    GameResponse getById(Long id);
    List<GameResponse> getAll();
    void delete(Long id);
}
