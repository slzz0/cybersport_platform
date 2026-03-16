package com.example.cybersport_platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Match response DTO")
public class MatchResponse {

    @Schema(description = "Match id", example = "1")
    private Long id;

    @Schema(description = "Tournament id", example = "1")
    private Long tournamentId;

    @Schema(description = "Team 1 id", example = "10")
    private Long team1Id;

    @Schema(description = "Team 2 id", example = "11")
    private Long team2Id;

    @Schema(description = "Team 1 score", example = "2")
    private Integer scoreTeam1;

    @Schema(description = "Team 2 score", example = "1")
    private Integer scoreTeam2;

    @Schema(description = "Played at", example = "2026-03-16T14:30:00")
    private LocalDateTime playedAt;
}
