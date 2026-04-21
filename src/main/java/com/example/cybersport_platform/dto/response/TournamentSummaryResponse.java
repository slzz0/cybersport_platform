package com.example.cybersport_platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Compact tournament response DTO")
public class TournamentSummaryResponse extends TournamentBaseResponse {

    public TournamentSummaryResponse(
            Long id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            String prizePool,
            Long gameId,
            String gameName
    ) {
        super(id, name, startDate, endDate, prizePool, gameId, gameName);
    }
}
