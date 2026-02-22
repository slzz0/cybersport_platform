package com.example.cybersport_platform.mapper;

import com.example.cybersport_platform.dto.response.PlayerResponse;
import com.example.cybersport_platform.model.Player;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlayerMapper {
    public PlayerResponse toResponse(Player player) {
        if (player == null) {
            return null;
        }
        Long teamId = player.getTeam() != null ? player.getTeam().getId() : null;
        String teamName = player.getTeam() != null ? player.getTeam().getName() : null;
        return new PlayerResponse(
                player.getId(),
                player.getNickname(),
                player.getElo(),
                teamId,
                teamName
        );
    }
}
