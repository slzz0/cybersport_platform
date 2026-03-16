package com.example.cybersport_platform.service;

import com.example.cybersport_platform.dto.request.MatchRequest;
import com.example.cybersport_platform.dto.response.MatchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchService {
    MatchResponse create(MatchRequest request);
    MatchResponse update(Long id, MatchRequest request);
    MatchResponse getById(Long id);
    List<MatchResponse> getAll();
    List<MatchResponse> getByTournamentId(Long tournamentId);
    Page<MatchResponse> getByFilters(
            String gameName,
            String tournamentName,
            LocalDateTime playedFrom,
            LocalDateTime playedTo,
            Pageable pageable,
            MatchSearchQueryType queryType
    );
    void delete(Long id);
}
