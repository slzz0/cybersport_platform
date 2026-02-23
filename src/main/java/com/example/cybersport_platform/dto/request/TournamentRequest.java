package com.example.cybersport_platform.dto.request;

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
@Schema(description = "Данные турнира")
public class TournamentRequest {
    @Schema(description = "Название турнира", example = "Major 2025", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Дата начала (YYYY-MM-DD)", example = "2025-01-10")
    private LocalDate startDate;

    @Schema(description = "Дата окончания (YYYY-MM-DD)", example = "2025-01-20")
    private LocalDate endDate;

    @Schema(description = "Призовой фонд", example = "$1,000,000")
    private String prizePool;

    @Schema(description = "ID игры", example = "1")
    private Long gameId;
}
