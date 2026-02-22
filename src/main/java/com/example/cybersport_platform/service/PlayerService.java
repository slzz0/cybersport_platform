package com.example.cybersport_platform.service;

import com.example.cybersport_platform.dto.request.PlayerRequest;
import com.example.cybersport_platform.dto.response.PlayerResponse;

import java.util.List;

public interface PlayerService {
    PlayerResponse create(PlayerRequest request);
    PlayerResponse update(Long id, PlayerRequest request);
    PlayerResponse getById(Long id);
    List<PlayerResponse> getAll();
    List<PlayerResponse> getByTeamId(Long teamId);
    List<PlayerResponse> getByGameId(Long gameId);
    List<PlayerResponse> searchByNickname(String nickname);
    void delete(Long id);
}
