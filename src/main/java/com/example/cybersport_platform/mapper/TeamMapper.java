package com.example.cybersport_platform.mapper;

import com.example.cybersport_platform.dto.response.TeamResponse;
import com.example.cybersport_platform.model.Team;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TeamMapper {
    public TeamResponse toResponse(Team team) {
        if (team == null) {
            return null;
        }
        Long gameId = team.getGame() != null ? team.getGame().getId() : null;
        String gameName = team.getGame() != null ? team.getGame().getName() : null;
        return new TeamResponse(team.getId(), team.getName(), gameId, gameName);
    }
}
