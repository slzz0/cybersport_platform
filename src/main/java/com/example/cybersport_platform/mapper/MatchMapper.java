package com.example.cybersport_platform.mapper;

import com.example.cybersport_platform.dto.response.MatchResponse;
import com.example.cybersport_platform.model.Match;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MatchMapper {
    public MatchResponse toResponse(Match match) {
        if (match == null) {
            return null;
        }
        Long tournamentId = match.getTournament() != null ? match.getTournament().getId() : null;
        Long team1Id = match.getTeam1() != null ? match.getTeam1().getId() : null;
        Long team2Id = match.getTeam2() != null ? match.getTeam2().getId() : null;
        return new MatchResponse(
                match.getId(),
                tournamentId,
                team1Id,
                team2Id,
                match.getScoreTeam1(),
                match.getScoreTeam2(),
                match.getPlayedAt()
        );
    }
}
