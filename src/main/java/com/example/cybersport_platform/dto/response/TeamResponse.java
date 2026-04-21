package com.example.cybersport_platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Team response DTO")
public class TeamResponse extends TeamBaseResponse {

    @Schema(description = "Related tournaments for this team")
    private List<TournamentSummaryResponse> tournaments = new ArrayList<>();

    public TeamResponse(
            Long id,
            String name,
            Long gameId,
            String gameName,
            List<TournamentSummaryResponse> tournaments
    ) {
        super(id, name, gameId, gameName);
        this.tournaments = tournaments != null ? tournaments : new ArrayList<>();
    }
}
