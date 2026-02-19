package com.example.cybersport_platform.mapper;

import com.example.cybersport_platform.dto.PlayerDto;
import com.example.cybersport_platform.model.Player;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlayerMapper {
    public PlayerDto toDto(Player player) {
        if (player == null) {
            return null;
        }

        return new PlayerDto(
            player.getId(),
            player.getNickname(),
            player.getTeam(),
            player.getGame(),
            player.getElo()
        );
    }

    public void updateEntity(Player player, PlayerDto dto) {
        player.setNickname(dto.getNickname());
        player.setTeam(dto.getTeam());
        player.setGame(dto.getGame());
        player.setElo(dto.getElo());
    }
}
