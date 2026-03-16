package com.example.cybersport_platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Match create/update request")
public class MatchRequest {

    @Schema(description = "Tournament id", example = "1")
    @NotNull(message = "Tournament id is required")
    @Positive(message = "Tournament id must be positive")
    private Long tournamentId;

    @Schema(description = "First team id", example = "10")
    @NotNull(message = "Team1 id is required")
    @Positive(message = "Team1 id must be positive")
    private Long team1Id;

    @Schema(description = "Second team id", example = "11")
    @NotNull(message = "Team2 id is required")
    @Positive(message = "Team2 id must be positive")
    private Long team2Id;

    @Schema(description = "First team score", example = "2")
    @NotNull(message = "scoreTeam1 is required")
    @Min(value = 0, message = "scoreTeam1 must be greater or equal to 0")
    private Integer scoreTeam1;

    @Schema(description = "Second team score", example = "1")
    @NotNull(message = "scoreTeam2 is required")
    @Min(value = 0, message = "scoreTeam2 must be greater or equal to 0")
    private Integer scoreTeam2;

    @Schema(description = "Match date/time", example = "2026-03-16T14:30:00")
    @NotNull(message = "playedAt is required")
    private LocalDateTime playedAt;
}
