package com.example.cybersport_platform.service.impl;

import com.example.cybersport_platform.dto.request.GameRequest;
import com.example.cybersport_platform.dto.response.GameResponse;
import com.example.cybersport_platform.exception.NotFoundException;
import com.example.cybersport_platform.mapper.GameMapper;
import com.example.cybersport_platform.model.Game;
import com.example.cybersport_platform.repository.GameRepository;
import com.example.cybersport_platform.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

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
        if (!gameRepository.existsById(id)) {
            throw new NotFoundException("Game not found: " + id);
        }
        gameRepository.deleteById(id);
    }
}
