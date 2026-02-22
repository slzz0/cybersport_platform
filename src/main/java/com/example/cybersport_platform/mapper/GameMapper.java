package com.example.cybersport_platform.mapper;

import com.example.cybersport_platform.dto.response.GameResponse;
import com.example.cybersport_platform.model.Game;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameMapper {
    public GameResponse toResponse(Game game) {
        if (game == null) {
            return null;
        }
        return new GameResponse(game.getId(), game.getName(), game.getDescription());
    }
}
