package com.example.cybersport_platform.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Team response DTO")
public class TeamResponse {

    @Schema(description = "Team id", example = "1")
    private Long id;

    @Schema(description = "Team name", example = "Team Spirit")
    private String name;

    @Schema(description = "Game id", example = "1")
    private Long gameId;

    @Schema(description = "Game name", example = "Dota 2")
    private String gameName;
}
