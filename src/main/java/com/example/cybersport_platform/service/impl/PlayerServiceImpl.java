package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.cache.MatchSearchIndex;
import com.example.cybersport_platform.dto.request.PlayerRequest;
import com.example.cybersport_platform.dto.response.PlayerResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.mapper.PlayerMapper;
import com.example.cybersport_platform.model.Player;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.repository.PlayerRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private static final String PLAYER_NOT_FOUND_MESSAGE = "Player not found: ";
    private static final String TEAM_NOT_FOUND_MESSAGE = "Team not found: ";
    private static final String GAME_NOT_FOUND_MESSAGE = "Game not found: ";

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final GameRepository gameRepository;
    private final MatchSearchIndex matchSearchIndex;

    @Override
    @Transactional
    public PlayerResponse create(PlayerRequest request) {
        Player player = buildPlayer(request, resolveTeam(request.getTeamId()));
        PlayerResponse response = PlayerMapper.toResponse(playerRepository.save(player));
        matchSearchIndex.invalidateAll();
        return response;
    }

    @Override
    @Transactional
    public List<PlayerResponse> createBulkTransactional(List<PlayerRequest> requests) {
        List<PlayerResponse> responses = createBulk(requests, false);
        matchSearchIndex.invalidateAll();
        return responses;
    }

    @Override
    public List<PlayerResponse> createBulkNonTransactional(List<PlayerRequest> requests) {
        List<PlayerResponse> responses = createBulk(requests, true);
        matchSearchIndex.invalidateAll();
        return responses;
    }

    @Override
    @Transactional
    public PlayerResponse update(Long id, PlayerRequest request) {
        Player existing = playerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PLAYER_NOT_FOUND_MESSAGE + id));
        existing.setNickname(request.getNickname());
        existing.setElo(request.getElo());
        existing.setTeam(resolveTeam(request.getTeamId()));
        PlayerResponse response = PlayerMapper.toResponse(playerRepository.save(existing));
        matchSearchIndex.invalidateAll();
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerResponse getById(Long id) {
        return playerRepository.findById(id)
                .map(PlayerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException(PLAYER_NOT_FOUND_MESSAGE + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerResponse> getAll() {
        return playerRepository.findAll().stream()
                .map(PlayerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerResponse> getByTeamId(Long teamId) {
        if (teamId == null) {
            return Collections.emptyList();
        }
        if (!teamRepository.existsById(teamId)) {
            throw new NotFoundException(TEAM_NOT_FOUND_MESSAGE + teamId);
        }
        return playerRepository.findByTeamId(teamId).stream()
                .map(PlayerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerResponse> getByGameId(Long gameId) {
        if (gameId == null) {
            return Collections.emptyList();
        }
        if (!gameRepository.existsById(gameId)) {
            throw new NotFoundException(GAME_NOT_FOUND_MESSAGE + gameId);
        }
        // fetch join — один запрос вместо N+1 при маппинге (getTeam().getName())
        return playerRepository.findByTeamGameIdWithTeam(gameId).stream()
                .map(PlayerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerResponse> searchByNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            return Collections.emptyList();
        }
        return playerRepository.findByNicknameContainingIgnoreCase(nickname).stream()
                .map(PlayerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new NotFoundException(PLAYER_NOT_FOUND_MESSAGE + id);
        }
        playerRepository.deleteById(id);
        matchSearchIndex.invalidateAll();
    }

    private List<PlayerResponse> createBulk(List<PlayerRequest> requests, boolean flushAfterEachSave) {
        Map<Long, com.example.cybersport_platform.model.Team> teamsById = getTeamsById(requests);
        return requests.stream()
                .map(request -> buildPlayer(request, getRequiredTeam(teamsById, request.getTeamId())))
                .map(player -> savePlayer(player, flushAfterEachSave))
                .map(PlayerMapper::toResponse)
                .toList();
    }

    private Map<Long, com.example.cybersport_platform.model.Team> getTeamsById(List<PlayerRequest> requests) {
        Set<Long> teamIds = requests.stream()
                .map(PlayerRequest::getTeamId)
                .flatMap(teamId -> Optional.ofNullable(teamId).stream())
                .collect(Collectors.toSet());

        return teamRepository.findAllById(teamIds).stream()
                .collect(Collectors.toMap(
                        com.example.cybersport_platform.model.Team::getId,
                        Function.identity(),
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    private Player buildPlayer(PlayerRequest request, com.example.cybersport_platform.model.Team team) {
        Player player = new Player();
        player.setNickname(request.getNickname());
        player.setElo(request.getElo());
        player.setTeam(team);
        return player;
    }

    private com.example.cybersport_platform.model.Team resolveTeam(Long teamId) {
        return Optional.ofNullable(teamId)
                .map(this::findTeamById)
                .orElse(null);
    }

    private com.example.cybersport_platform.model.Team getRequiredTeam(
            Map<Long, com.example.cybersport_platform.model.Team> teamsById,
            Long teamId
    ) {
        return Optional.ofNullable(teamsById.get(teamId))
                .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + teamId));
    }

    private com.example.cybersport_platform.model.Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException(TEAM_NOT_FOUND_MESSAGE + teamId));
    }

    private Player savePlayer(Player player, boolean flushAfterEachSave) {
        return flushAfterEachSave
                ? playerRepository.saveAndFlush(player)
                : playerRepository.save(player);
    }
}
