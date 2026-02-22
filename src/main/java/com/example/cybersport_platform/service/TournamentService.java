package com.example.cybersport_platform.service;

import com.example.cybersport_platform.dto.request.TournamentRequest;
import com.example.cybersport_platform.dto.response.TournamentResponse;

import java.util.List;

public interface TournamentService {
    TournamentResponse create(TournamentRequest request);
    TournamentResponse update(Long id, TournamentRequest request);
    TournamentResponse getById(Long id);
    List<TournamentResponse> getAll();
    List<TournamentResponse> getByGameId(Long gameId);
    void delete(Long id);
}
