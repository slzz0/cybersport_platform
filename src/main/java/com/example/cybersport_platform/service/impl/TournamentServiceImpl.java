package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.TournamentRequest;
import com.example.cybersport_platform.dto.response.TournamentResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.mapper.TournamentMapper;
import com.example.cybersport_platform.model.Tournament;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.TournamentRepository;
import com.example.cybersport_platform.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {

    private static final String TOURNAMENT_NOT_FOUND_MESSAGE = "Tournament not found: ";
    private static final String GAME_NOT_FOUND_MESSAGE = "Game not found: ";

    private final TournamentRepository tournamentRepository;
    private final GameRepository gameRepository;
    private final MatchSearchIndex matchSearchIndex;

    @Override
    @Transactional
    public TournamentResponse create(TournamentRequest request) {
        Tournament tournament = new Tournament();
        tournament.setName(request.getName());
        tournament.setStartDate(request.getStartDate());
        tournament.setEndDate(request.getEndDate());
        tournament.setPrizePool(request.getPrizePool());
        if (request.getGameId() != null) {
            tournament.setGame(gameRepository.findById(request.getGameId())
                    .orElseThrow(() -> new NotFoundException(GAME_NOT_FOUND_MESSAGE + request.getGameId())));
        }
        TournamentResponse response = TournamentMapper.toResponse(tournamentRepository.save(tournament));
        matchSearchIndex.invalidateAll();
        return response;
    }

    @Override
    @Transactional
    public TournamentResponse update(Long id, TournamentRequest request) {
        Tournament existing = tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND_MESSAGE + id));
        existing.setName(request.getName());
        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());
        existing.setPrizePool(request.getPrizePool());
        if (request.getGameId() != null) {
            existing.setGame(gameRepository.findById(request.getGameId())
                    .orElseThrow(() -> new NotFoundException(GAME_NOT_FOUND_MESSAGE + request.getGameId())));
        } else {
            existing.setGame(null);
        }
        TournamentResponse response = TournamentMapper.toResponse(tournamentRepository.save(existing));
        matchSearchIndex.invalidateAll();
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public TournamentResponse getById(Long id) {
        return tournamentRepository.findById(id)
                .map(TournamentMapper::toResponse)
                .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND_MESSAGE + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TournamentResponse> getAll() {
        return tournamentRepository.findAll().stream()
                .map(TournamentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TournamentResponse> getByGameId(Long gameId) {
        if (gameId == null) {
            return List.of();
        }
        if (!gameRepository.existsById(gameId)) {
            throw new NotFoundException(GAME_NOT_FOUND_MESSAGE + gameId);
        }
        return tournamentRepository.findByGameId(gameId).stream()
                .map(TournamentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TOURNAMENT_NOT_FOUND_MESSAGE + id));

        // Remove many-to-many join rows from both sides before deleting tournament.
        tournament.getTeams().forEach(team -> team.getTournaments().remove(tournament));
        tournament.getTeams().clear();

        tournamentRepository.delete(tournament);
        matchSearchIndex.invalidateAll();
    }
}
