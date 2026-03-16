package com.example.cybersport_platform.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request for creating game with teams")
public class GameWithTeamsRequest {

    @Schema(description = "Game name", example = "Counter-Strike 2")
    @NotBlank(message = "Game name is required")
    @Size(max = 120, message = "Game name must be at most 120 characters")
    private String gameName;

    @Schema(description = "Game description", example = "Tactical shooter")
    @Size(max = 1000, message = "Game description must be at most 1000 characters")
    private String gameDescription;

    @Schema(description = "Team names", example = "[\"NAVI\", \"G2\"]")
    @NotEmpty(message = "teamNames must contain at least one name")
    private List<@NotBlank(message = "Team name must not be blank") String> teamNames;

    @Schema(description = "Optional team id for forced lookup", example = "1")
    @Positive(message = "teamId must be positive")
    private Long teamId;
}
