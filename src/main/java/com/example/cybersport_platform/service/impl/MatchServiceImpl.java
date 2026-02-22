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
            tournamentRepository.findById(request.getTournamentId()).ifPresent(match::setTournament);
        }
        if (request.getTeam1Id() != null) {
            teamRepository.findById(request.getTeam1Id()).ifPresent(match::setTeam1);
        }
        if (request.getTeam2Id() != null) {
            teamRepository.findById(request.getTeam2Id()).ifPresent(match::setTeam2);
        }
        return MatchMapper.toResponse(matchRepository.save(match));
    }

    @Override
    @Transactional
    public MatchResponse update(Long id, MatchRequest request) {
        Match existing = matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Match not found: " + id));
        existing.setScoreTeam1(request.getScoreTeam1());
        existing.setScoreTeam2(request.getScoreTeam2());
        existing.setPlayedAt(request.getPlayedAt());
        if (request.getTournamentId() != null) {
            tournamentRepository.findById(request.getTournamentId())
                    .ifPresentOrElse(existing::setTournament, () -> existing.setTournament(null));
        }
        if (request.getTeam1Id() != null) {
            teamRepository.findById(request.getTeam1Id())
                    .ifPresentOrElse(existing::setTeam1, () -> existing.setTeam1(null));
        }
        if (request.getTeam2Id() != null) {
            teamRepository.findById(request.getTeam2Id())
                    .ifPresentOrElse(existing::setTeam2, () -> existing.setTeam2(null));
        }
        return MatchMapper.toResponse(matchRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public MatchResponse getById(Long id) {
        return matchRepository.findById(id)
                .map(MatchMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Match not found: " + id));
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
        return matchRepository.findByTournamentId(tournamentId).stream()
                .map(MatchMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new NotFoundException("Match not found: " + id);
        }
        matchRepository.deleteById(id);
    }
}
