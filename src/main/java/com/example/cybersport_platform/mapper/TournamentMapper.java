package com.example.cybersport_platform.mapper;

import com.example.cybersport_platform.dto.response.TournamentResponse;
import com.example.cybersport_platform.model.Tournament;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TournamentMapper {
    public TournamentResponse toResponse(Tournament tournament) {
        if (tournament == null) {
            return null;
        }
        Long gameId = tournament.getGame() != null ? tournament.getGame().getId() : null;
        String gameName = tournament.getGame() != null ? tournament.getGame().getName() : null;
        return new TournamentResponse(
                tournament.getId(),
                tournament.getName(),
                tournament.getStartDate(),
                tournament.getEndDate(),
                tournament.getPrizePool(),
                gameId,
                gameName
        );
    }
}
