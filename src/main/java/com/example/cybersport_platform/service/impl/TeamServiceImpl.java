package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.TeamRequest;
import com.example.cybersport_platform.dto.response.TeamResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.mapper.TeamMapper;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;

    @Override
    @Transactional
    public TeamResponse create(TeamRequest request) {
        Team team = new Team();
        team.setName(request.getName());
        if (request.getGameId() != null) {
            gameRepository.findById(request.getGameId()).ifPresent(team::setGame);
        }
        return TeamMapper.toResponse(teamRepository.save(team));
    }

    @Override
    @Transactional
    public TeamResponse update(Long id, TeamRequest request) {
        Team existing = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Team not found: " + id));
        existing.setName(request.getName());
        if (request.getGameId() != null) {
            gameRepository.findById(request.getGameId())
                    .ifPresentOrElse(existing::setGame, () -> existing.setGame(null));
        } else {
            existing.setGame(null);
        }
        return TeamMapper.toResponse(teamRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponse getById(Long id) {
        return teamRepository.findById(id)
                .map(TeamMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Team not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getAll() {
        return teamRepository.findAll().stream()
                .map(TeamMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getByGameId(Long gameId) {
        if (gameId == null) {
            return List.of();
        }
        return teamRepository.findByGameId(gameId).stream()
                .map(TeamMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new NotFoundException("Team not found: " + id);
        }
        teamRepository.deleteById(id);
    }
}
