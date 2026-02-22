package com.example.cybersport_platform.service;

import com.example.cybersport_platform.dto.request.TeamRequest;
import com.example.cybersport_platform.dto.response.TeamResponse;

import java.util.List;

public interface TeamService {
    TeamResponse create(TeamRequest request);
    TeamResponse update(Long id, TeamRequest request);
    TeamResponse getById(Long id);
    List<TeamResponse> getAll();
    List<TeamResponse> getByGameId(Long gameId);
    void delete(Long id);
}
