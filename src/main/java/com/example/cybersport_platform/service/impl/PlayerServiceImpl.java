package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.PlayerRequest;
import com.example.cybersport_platform.dto.response.PlayerResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.mapper.PlayerMapper;
import com.example.cybersport_platform.model.Player;
import com.example.cybersport_platform.repository.PlayerRepository;
import com.example.cybersport_platform.repository.TeamRepository;
import com.example.cybersport_platform.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public PlayerResponse create(PlayerRequest request) {
        Player player = new Player();
        player.setNickname(request.getNickname());
        player.setElo(request.getElo());
        if (request.getTeamId() != null) {
            teamRepository.findById(request.getTeamId()).ifPresent(player::setTeam);
        }
        return PlayerMapper.toResponse(playerRepository.save(player));
    }

    @Override
    @Transactional
    public PlayerResponse update(Long id, PlayerRequest request) {
        Player existing = playerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Player not found: " + id));
        existing.setNickname(request.getNickname());
        existing.setElo(request.getElo());
        if (request.getTeamId() != null) {
            teamRepository.findById(request.getTeamId())
                    .ifPresentOrElse(existing::setTeam, () -> existing.setTeam(null));
        } else {
            existing.setTeam(null);
        }
        return PlayerMapper.toResponse(playerRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerResponse getById(Long id) {
        return playerRepository.findById(id)
                .map(PlayerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Player not found: " + id));
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
            throw new NotFoundException("Player not found: " + id);
        }
        playerRepository.deleteById(id);
    }
}
