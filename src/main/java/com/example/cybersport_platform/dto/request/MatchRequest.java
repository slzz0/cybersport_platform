package com.example.cybersport_platform.dto.request;

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
@Schema(description = "Данные матча")
public class MatchRequest {
    @Schema(description = "ID турнира", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tournamentId;

    @Schema(description = "ID первой команды", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long team1Id;

    @Schema(description = "ID второй команды", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long team2Id;

    @Schema(description = "Счёт первой команды", example = "2")
    private Integer scoreTeam1;

    @Schema(description = "Счёт второй команды", example = "1")
    private Integer scoreTeam2;

    @Schema(description = "Дата и время матча (YYYY-MM-DDTHH:mm:ss)", example = "2025-01-15T18:00:00")
    private LocalDateTime playedAt;
}
