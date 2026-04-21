package com.example.cybersport_platform.mapper;

import com.example.cybersport_platform.dto.response.TournamentResponse;
import com.example.cybersport_platform.dto.response.TournamentSummaryResponse;
import com.example.cybersport_platform.dto.response.TeamSummaryResponse;
import com.example.cybersport_platform.model.Tournament;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.stream.Stream;

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
                gameName,
                collectTeams(tournament)
        );
    }

    public TournamentSummaryResponse toSummaryResponse(Tournament tournament) {
        if (tournament == null) {
            return null;
        }
        Long gameId = tournament.getGame() != null ? tournament.getGame().getId() : null;
        String gameName = tournament.getGame() != null ? tournament.getGame().getName() : null;
        return new TournamentSummaryResponse(
                tournament.getId(),
                tournament.getName(),
                tournament.getStartDate(),
                tournament.getEndDate(),
                tournament.getPrizePool(),
                gameId,
                gameName
        );
    }

    private java.util.List<TeamSummaryResponse> collectTeams(Tournament tournament) {
        LinkedHashMap<Long, TeamSummaryResponse> summaries = new LinkedHashMap<>();

        Stream.concat(
                        tournament.getTeams().stream(),
                        tournament.getMatches().stream()
                                .flatMap(match -> Stream.of(match.getTeam1(), match.getTeam2()))
                )
                .filter(team -> team != null && team.getId() != null)
                .map(TeamMapper::toSummaryResponse)
                .forEach(summary -> summaries.put(summary.getId(), summary));

        return summaries.values().stream().toList();
    }
}
