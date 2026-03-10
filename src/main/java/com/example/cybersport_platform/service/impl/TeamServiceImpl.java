package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.TeamRequest;
import com.example.cybersport_platform.dto.response.TeamResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.mapper.TeamMapper;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.MatchRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private static final String TEAM_NOT_FOUND_MESSAGE = "Team not found: ";
    private static final String GAME_NOT_FOUND_MESSAGE = "Game not found: ";

    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;
    private final MatchRepository matchRepository;

    @Override
    @Transactional
    public TeamResponse create(TeamRequest request) {
        Team team = new Team();
        team.setName(request.getName());
        if (request.getGameId() != null) {
            team.setGame(gameRepository.findById(request.getGameId())
                    .orElseThrow(() -> new NotFoundException(GAME_NOT_FOUND_MESSAGE + request.getGameId())));
        }
        return TeamMapper.toResponse(teamRepository.save(team));
    }

    @Override
    @Transactional
    public TeamResponse update(Long id, TeamRequest request) {
        Team existing = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + id));
        existing.setName(request.getName());
        if (request.getGameId() != null) {
            existing.setGame(gameRepository.findById(request.getGameId())
                    .orElseThrow(() -> new NotFoundException(GAME_NOT_FOUND_MESSAGE + request.getGameId())));
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
                .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + id));
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
        if (!gameRepository.existsById(gameId)) {
            throw new NotFoundException(GAME_NOT_FOUND_MESSAGE + gameId);
        }
        return teamRepository.findByGameId(gameId).stream()
                .map(TeamMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + id));

        // Remove matches where team participates as team1/team2 to avoid FK violations.
        matchRepository.deleteByTeam1IdOrTeam2Id(id, id);

        // Clear many-to-many links from both sides before deleting team.
        team.getTournaments().forEach(tournament -> tournament.getTeams().remove(team));
        team.getTournaments().clear();

        teamRepository.delete(team);
    }
}
