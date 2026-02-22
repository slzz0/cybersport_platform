package com.example.cybersport_platform.service;

import com.example.cybersport_platform.dto.request.MatchRequest;
import com.example.cybersport_platform.dto.response.MatchResponse;

import java.util.List;

public interface MatchService {
    MatchResponse create(MatchRequest request);
    MatchResponse update(Long id, MatchRequest request);
    MatchResponse getById(Long id);
    List<MatchResponse> getAll();
    List<MatchResponse> getByTournamentId(Long tournamentId);
    void delete(Long id);
}
