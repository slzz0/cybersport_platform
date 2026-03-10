package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.MatchRequest;
import com.example.cybersport_platform.dto.response.MatchResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.mapper.MatchMapper;
import com.example.cybersport_platform.model.Match;
import com.example.cybersport_platform.repository.MatchRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.repository.TournamentRepository;
import com.example.cybersport_platform.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private static final String MATCH_NOT_FOUND_MESSAGE = "Match not found: ";
    private static final String TEAM_NOT_FOUND_MESSAGE = "Team not found: ";
    private static final String TOURNAMENT_NOT_FOUND_MESSAGE = "Tournament not found: ";

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public MatchResponse create(MatchRequest request) {
        Match match = new Match();
        match.setScoreTeam1(request.getScoreTeam1());
        match.setScoreTeam2(request.getScoreTeam2());
        match.setPlayedAt(request.getPlayedAt());
        if (request.getTournamentId() != null) {
            match.setTournament(tournamentRepository.findById(request.getTournamentId())
                    .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND_MESSAGE + request.getTournamentId())));
        }
        if (request.getTeam1Id() != null) {
            match.setTeam1(teamRepository.findById(request.getTeam1Id())
                    .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + request.getTeam1Id())));
        }
        if (request.getTeam2Id() != null) {
            match.setTeam2(teamRepository.findById(request.getTeam2Id())
                    .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + request.getTeam2Id())));
        }
        return MatchMapper.toResponse(matchRepository.save(match));
    }

    @Override
    @Transactional
    public MatchResponse update(Long id, MatchRequest request) {
        Match existing = matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MATCH_NOT_FOUND_MESSAGE + id));
        existing.setScoreTeam1(request.getScoreTeam1());
        existing.setScoreTeam2(request.getScoreTeam2());
        existing.setPlayedAt(request.getPlayedAt());
        if (request.getTournamentId() != null) {
            existing.setTournament(tournamentRepository.findById(request.getTournamentId())
                    .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND_MESSAGE + request.getTournamentId())));
        } else {
            existing.setTournament(null);
        }
        if (request.getTeam1Id() != null) {
            existing.setTeam1(teamRepository.findById(request.getTeam1Id())
                    .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + request.getTeam1Id())));
        } else {
            existing.setTeam1(null);
        }
        if (request.getTeam2Id() != null) {
            existing.setTeam2(teamRepository.findById(request.getTeam2Id())
                    .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + request.getTeam2Id())));
        } else {
            existing.setTeam2(null);
        }
        return MatchMapper.toResponse(matchRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public MatchResponse getById(Long id) {
        return matchRepository.findById(id)
                .map(MatchMapper::toResponse)
                .orElseThrow(() -> new NotFoundException(MATCH_NOT_FOUND_MESSAGE + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchResponse> getAll() {
        return matchRepository.findAll().stream()
                .map(MatchMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchResponse> getByTournamentId(Long tournamentId) {
        if (tournamentId == null) {
            return List.of();
        }
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new NotFoundException(TOURNAMENT_NOT_FOUND_MESSAGE + tournamentId);
        }
        return matchRepository.findByTournamentId(tournamentId).stream()
                .map(MatchMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new NotFoundException(MATCH_NOT_FOUND_MESSAGE + id);
        }
        matchRepository.deleteById(id);
    }
}
