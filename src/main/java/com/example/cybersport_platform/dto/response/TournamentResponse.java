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
@Schema(description = "Tournament response DTO")
public class TournamentResponse extends TournamentBaseResponse {

    @Schema(description = "Participating teams for this tournament")
    private List<TeamSummaryResponse> teams = new ArrayList<>();

    public TournamentResponse(
            Long id,
            String name,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate,
            String prizePool,
            Long gameId,
            String gameName,
            List<TeamSummaryResponse> teams
    ) {
        super(id, name, startDate, endDate, prizePool, gameId, gameName);
        this.teams = teams != null ? teams : new ArrayList<>();
    }
}
