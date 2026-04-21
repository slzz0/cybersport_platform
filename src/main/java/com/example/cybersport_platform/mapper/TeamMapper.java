package com.example.cybersport_platform.mapper;

import com.example.cybersport_platform.dto.response.TeamResponse;
import com.example.cybersport_platform.dto.response.TeamSummaryResponse;
import com.example.cybersport_platform.dto.response.TournamentSummaryResponse;
import com.example.cybersport_platform.model.Match;
import com.example.cybersport_platform.model.Team;
import com.example.cybersport_platform.model.Tournament;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.stream.Stream;

@UtilityClass
public class TeamMapper {
    public TeamResponse toResponse(Team team) {
        if (team == null) {
            return null;
        }
        Long gameId = team.getGame() != null ? team.getGame().getId() : null;
        String gameName = team.getGame() != null ? team.getGame().getName() : null;
        return new TeamResponse(
                team.getId(),
                team.getName(),
                gameId,
                gameName,
                collectTournaments(team)
        );
    }

    public TeamSummaryResponse toSummaryResponse(Team team) {
        if (team == null) {
            return null;
        }
        Long gameId = team.getGame() != null ? team.getGame().getId() : null;
        String gameName = team.getGame() != null ? team.getGame().getName() : null;
        return new TeamSummaryResponse(team.getId(), team.getName(), gameId, gameName);
    }

    private java.util.List<TournamentSummaryResponse> collectTournaments(Team team) {
        LinkedHashMap<Long, TournamentSummaryResponse> summaries = new LinkedHashMap<>();

        Stream.concat(
                        team.getTournaments().stream(),
                        Stream.concat(team.getMatchesAsTeam1().stream(), team.getMatchesAsTeam2().stream())
                                .map(Match::getTournament)
                )
                .filter(tournament -> tournament != null && tournament.getId() != null)
                .map(TournamentMapper::toSummaryResponse)
                .forEach(summary -> summaries.put(summary.getId(), summary));

        return summaries.values().stream().toList();
    }
}
