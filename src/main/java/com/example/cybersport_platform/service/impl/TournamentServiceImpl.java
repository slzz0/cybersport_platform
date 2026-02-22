package com.example.cybersport_platform.service.impl;

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

    private final TournamentRepository tournamentRepository;
    private final GameRepository gameRepository;

    @Override
    @Transactional
    public TournamentResponse create(TournamentRequest request) {
        Tournament tournament = new Tournament();
        tournament.setName(request.getName());
        tournament.setStartDate(request.getStartDate());
        tournament.setEndDate(request.getEndDate());
        tournament.setPrizePool(request.getPrizePool());
        if (request.getGameId() != null) {
            gameRepository.findById(request.getGameId()).ifPresent(tournament::setGame);
        }
        return TournamentMapper.toResponse(tournamentRepository.save(tournament));
    }

    @Override
    @Transactional
    public TournamentResponse update(Long id, TournamentRequest request) {
        Tournament existing = tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tournament not found: " + id));
        existing.setName(request.getName());
        existing.setStartDate(request.getStartDate());
        existing.setEndDate(request.getEndDate());
        existing.setPrizePool(request.getPrizePool());
        if (request.getGameId() != null) {
            gameRepository.findById(request.getGameId())
                    .ifPresentOrElse(existing::setGame, () -> existing.setGame(null));
        } else {
            existing.setGame(null);
        }
        return TournamentMapper.toResponse(tournamentRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public TournamentResponse getById(Long id) {
        return tournamentRepository.findById(id)
                .map(TournamentMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Tournament not found: " + id));
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
        return tournamentRepository.findByGameId(gameId).stream()
                .map(TournamentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!tournamentRepository.existsById(id)) {
            throw new NotFoundException("Tournament not found: " + id);
        }
        tournamentRepository.deleteById(id);
    }
}
