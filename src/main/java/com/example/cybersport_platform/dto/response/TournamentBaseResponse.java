package com.example.cybersport_platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentBaseResponse {

    @Schema(description = "Tournament id", example = "1")
    private Long id;

    @Schema(description = "Tournament name", example = "The International")
    private String name;

    @Schema(description = "Start date", example = "2026-08-15")
    private LocalDate startDate;

    @Schema(description = "End date", example = "2026-08-25")
    private LocalDate endDate;

    @Schema(description = "Prize pool", example = "$1,000,000")
    private String prizePool;

    @Schema(description = "Game id", example = "1")
    private Long gameId;

    @Schema(description = "Game name", example = "Dota 2")
    private String gameName;
}
