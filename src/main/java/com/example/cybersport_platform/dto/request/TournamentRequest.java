package com.example.cybersport_platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Tournament create/update request")
public class TournamentRequest {

    @Schema(description = "Tournament name", example = "The International")
    @NotBlank(message = "Tournament name is required")
    @Size(max = 150, message = "Tournament name must be at most 150 characters")
    private String name;

    @Schema(description = "Start date", example = "2026-08-15")
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @Schema(description = "End date", example = "2026-08-25")
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Schema(description = "Prize pool", example = "$1,000,000")
    @NotBlank(message = "Prize pool is required")
    @Size(max = 120, message = "Prize pool must be at most 120 characters")
    private String prizePool;

    @Schema(description = "Related game id", example = "1")
    @NotNull(message = "Game id is required")
    @Positive(message = "Game id must be positive")
    private Long gameId;
}
