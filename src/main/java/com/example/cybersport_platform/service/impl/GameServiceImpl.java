package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.GameRequest;
import com.example.cybersport_platform.dto.response.GameResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.mapper.GameMapper;
import com.example.cybersport_platform.model.Game;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.model.Tournament;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.MatchRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.repository.TournamentRepository;
import com.example.cybersport_platform.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;

    @Override
    @Transactional
    public GameResponse create(GameRequest request) {
        Game game = new Game();
        game.setName(request.getName());
        game.setDescription(request.getDescription());
        return GameMapper.toResponse(gameRepository.save(game));
    }

    @Override
    @Transactional
    public GameResponse update(Long id, GameRequest request) {
        Game existing = gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Game not found: " + id));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        return GameMapper.toResponse(gameRepository.save(existing));
    }

    @Override
    public GameResponse getById(Long id) {
        return gameRepository.findById(id)
                .map(GameMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Game not found: " + id));
    }

    @Override
    public List<GameResponse> getAll() {
        return gameRepository.findAll().stream()
                .map(GameMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Game not found: " + id));

        List<Team> teams = teamRepository.findByGameId(id);
        List<Tournament> tournaments = tournamentRepository.findByGameId(id);

        teams.forEach(team -> {
            team.getTournaments().forEach(tournament -> tournament.getTeams().remove(team));
            team.getTournaments().clear();
        });

        tournaments.forEach(tournament -> {
            tournament.getTeams().forEach(team -> team.getTournaments().remove(tournament));
            tournament.getTeams().clear();
        });

        Set<Long> teamIds = teams.stream()
                .map(Team::getId)
                .collect(java.util.stream.Collectors.toSet());
        Set<Long> tournamentIds = tournaments.stream()
                .map(Tournament::getId)
                .collect(java.util.stream.Collectors.toSet());

        if (!teamIds.isEmpty()) {
            matchRepository.deleteByTeam1IdInOrTeam2IdIn(teamIds, teamIds);
        }
        if (!tournamentIds.isEmpty()) {
            matchRepository.deleteByTournamentIdIn(tournamentIds);
        }

        tournamentRepository.deleteAll(tournaments);
        teamRepository.deleteAll(teams);

        gameRepository.delete(game);
    }
}
